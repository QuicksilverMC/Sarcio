package dev.rdh.sarcio.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rdh.sarcio.SarcioMod;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.network.PacketByteBuf;

public final class LongChatDetector {
    private static final int PROTOCOL = 315; // 1.11
    private static final int TIMEOUT_MILLIS = 4000;

    private static final State NONE = new State(null, false);
    private static final AtomicReference<State> STATE = new AtomicReference<>(NONE);

    private record State(String ip, boolean supported) {
    }

    private LongChatDetector() {
    }

    public static boolean supported() {
        return STATE.get().supported;
    }

    public static void detect(String serverIp) {
        if (Objects.equals(serverIp, STATE.get().ip)) {
            return;
        }
        State pending = new State(serverIp, false);
        STATE.set(pending);
        if (serverIp == null) {
            return;
        }
        Thread.ofVirtual().name("Long Chat Detector").start(() -> {
            try {
                STATE.compareAndSet(pending, new State(serverIp, ping(serverIp) >= PROTOCOL));
            } catch (Exception e) {
                SarcioMod.LOGGER.warn("Could not detect long chat support for {}", serverIp, e);
                STATE.compareAndSet(pending, NONE);
            }
        });
    }

    private static int ping(String serverIp) throws IOException {
        ServerAddress address = ServerAddress.parse(serverIp);
        try (Socket socket = new Socket()) {
            socket.setSoTimeout(TIMEOUT_MILLIS);
            socket.connect(new InetSocketAddress(address.getAddress(), address.getPort()), TIMEOUT_MILLIS);

            PacketByteBuf handshake = new PacketByteBuf(Unpooled.buffer());
            handshake.writeVarInt(0);
            handshake.writeVarInt(PROTOCOL);
            handshake.writeString(address.getAddress());
            handshake.writeShort(address.getPort());
            handshake.writeVarInt(1);

            PacketByteBuf out = new PacketByteBuf(Unpooled.buffer());
            out.writeVarInt(handshake.readableBytes());
            out.writeBytes(handshake);
            out.writeVarInt(1);
            out.writeVarInt(0);
            out.readBytes(socket.getOutputStream(), out.readableBytes());

            InputStream in = socket.getInputStream();
            int length = 0;
            for (int shift = 0; ; shift += 7) {
                int b = shift > 14 ? -1 : in.read();
                if (b < 0) {
                    throw new IOException("Bad status response");
                }
                length |= (b & 0x7F) << shift;
                if ((b & 0x80) == 0) {
                    break;
                }
            }

            PacketByteBuf response = new PacketByteBuf(Unpooled.wrappedBuffer(in.readNBytes(length)));
            response.readVarInt();
            JsonObject version = JsonParser.parseString(response.readString(32767)).getAsJsonObject().getAsJsonObject("version");
            return version != null && version.has("protocol") ? version.get("protocol").getAsInt() : 0;
        }
    }
}
