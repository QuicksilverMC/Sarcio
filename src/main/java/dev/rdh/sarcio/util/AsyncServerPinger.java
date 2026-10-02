package dev.rdh.sarcio.util;

import dev.rdh.sarcio.mixin.tweaks.ServerAddressAccessor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.dns.DefaultDnsQuestion;
import io.netty.handler.codec.dns.DefaultDnsRecordDecoder;
import io.netty.handler.codec.dns.DnsRawRecord;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.resolver.dns.DnsNameResolver;
import io.netty.resolver.dns.DnsNameResolverBuilder;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import java.net.IDN;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.options.ServerListEntry;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.Connection;
import net.minecraft.text.Formatting;

public final class AsyncServerPinger {
    private static final int TIMEOUT_MILLIS = 4000;
    private static final int MAX_PINGS_IN_FLIGHT = 64;
    private static final AtomicInteger IN_FLIGHT = new AtomicInteger();

    public static final ThreadLocal<Ping> CURRENT = new ThreadLocal<>();

    public static final class Ping {
        public String srvHost;
        public ServerAddress address;
        public InetAddress inet;
        public Connection connection;
        public ChannelFuture connecting;
        private volatile boolean timedOut;
    }

    private static final class Loop {
        static final EventLoop LOOP = Connection.NETWORK_GROUP.get().next();
    }

    private static final class Dns {
        static final DnsNameResolver RESOLVER = new DnsNameResolverBuilder(Loop.LOOP)
            .datagramChannelType(NioDatagramChannel.class)
            .queryTimeoutMillis(TIMEOUT_MILLIS)
            .build();
    }

    private AsyncServerPinger() {
    }

    public static void ping(ServerListEntry entry, Runnable vanillaPing) {
        if (IN_FLIGHT.incrementAndGet() > MAX_PINGS_IN_FLIGHT) {
            fail(entry, Formatting.GRAY + "Spamming...");
            return;
        }
        Loop.LOOP.execute(() -> {
            try {
                start(entry, vanillaPing);
            } catch (RuntimeException e) {
                fail(entry, Formatting.DARK_RED + "Can't connect to server.");
            }
        });
    }

    private static void start(ServerListEntry entry, Runnable vanillaPing) {
        Ping ping = new Ping();
        ServerAddress parsed;
        CURRENT.set(ping);
        try {
            parsed = ServerAddress.parse(entry.ip);
        } finally {
            CURRENT.remove();
        }

        if (parsed == null) {
            fail(entry, Formatting.DARK_RED + "Can't connect to server.");
        } else if (ping.srvHost == null) {
            resolve(entry, ping, parsed, vanillaPing);
        } else {
            DefaultDnsQuestion question = new DefaultDnsQuestion("_minecraft._tcp." + IDN.toASCII(ping.srvHost), DnsRecordType.SRV);
            Dns.RESOLVER.resolveAll(question).addListener((FutureListener<List<DnsRecord>>) srv -> {
                ServerAddress address = parsed;
                if (srv.isSuccess()) {
                    for (DnsRecord record : srv.getNow()) {
                        try {
                            if (address == parsed && record instanceof DnsRawRecord raw) {
                                ByteBuf data = raw.content().duplicate().skipBytes(4);
                                int port = data.readUnsignedShort();
                                address = ServerAddressAccessor.create(DefaultDnsRecordDecoder.decodeName(data), port);
                            }
                        } catch (RuntimeException ignored) {
                        } finally {
                            ReferenceCountUtil.release(record);
                        }
                    }
                }
                resolve(entry, ping, address, vanillaPing);
            });
        }
    }

    private static void resolve(ServerListEntry entry, Ping ping, ServerAddress address, Runnable vanillaPing) {
        ping.address = address;
        Dns.RESOLVER.resolve(address.getAddress()).addListener((FutureListener<InetAddress>) resolved -> {
            if (resolved.isSuccess()) {
                ping.inet = resolved.getNow();
                connect(entry, ping, vanillaPing);
                return;
            }
            Thread.ofVirtual().name("Server Pinger DNS").start(() -> {
                try {
                    ping.inet = InetAddress.getByName(address.getAddress());
                } catch (UnknownHostException e) {
                    fail(entry, Formatting.DARK_RED + "Can't resolve hostname");
                    return;
                }
                connect(entry, ping, vanillaPing);
            });
        });
    }

    private static void connect(ServerListEntry entry, Ping ping, Runnable vanillaPing) {
        CURRENT.set(ping);
        try {
            ping.connection = Connection.connect(ping.inet, ping.address.getPort(), false);
        } catch (Exception e) {
            fail(entry, Formatting.DARK_RED + "Can't connect to server.");
            return;
        } finally {
            CURRENT.remove();
        }

        ChannelFuture connecting = ping.connecting;
        ScheduledFuture<?> timeout = connecting.channel().eventLoop().schedule(() -> {
            ping.timedOut = true;
            connecting.channel().close();
        }, TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        connecting.addListener((ChannelFutureListener) connected -> {
            timeout.cancel(false);
            if (!connected.isSuccess()) {
                fail(entry, ping.timedOut ? Formatting.RED + I18n.translate("disconnect.timeout") : Formatting.DARK_RED + "Can't connect to server.");
                return;
            }
            IN_FLIGHT.decrementAndGet();
            connected.channel().eventLoop().execute(() -> {
                CURRENT.set(ping);
                try {
                    vanillaPing.run();
                } finally {
                    CURRENT.remove();
                }
            });
        });
    }

    private static void fail(ServerListEntry entry, String reason) {
        IN_FLIGHT.decrementAndGet();
        entry.ping = -1L;
        entry.motd = reason;
    }
}
