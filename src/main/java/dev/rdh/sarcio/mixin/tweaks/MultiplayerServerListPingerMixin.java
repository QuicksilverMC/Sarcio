package dev.rdh.sarcio.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rdh.sarcio.util.AsyncServerPinger;
import java.net.InetAddress;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiplayerServerListPinger.class)
public class MultiplayerServerListPingerMixin {
    @WrapOperation(method = "add", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ServerAddress;parse(Ljava/lang/String;)Lnet/minecraft/client/network/ServerAddress;"))
    private ServerAddress sarcio$useResolvedAddress(String ip, Operation<ServerAddress> original) {
        AsyncServerPinger.Ping ping = AsyncServerPinger.CURRENT.get();
        return ping != null ? ping.address : original.call(ip);
    }

    @WrapOperation(method = "add", at = @At(value = "INVOKE", target = "Ljava/net/InetAddress;getByName(Ljava/lang/String;)Ljava/net/InetAddress;"))
    private InetAddress sarcio$useResolvedInet(String host, Operation<InetAddress> original) {
        AsyncServerPinger.Ping ping = AsyncServerPinger.CURRENT.get();
        return ping != null ? ping.inet : original.call(host);
    }

    @WrapOperation(method = "add", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;connect(Ljava/net/InetAddress;IZ)Lnet/minecraft/network/Connection;"))
    private Connection sarcio$useOpenConnection(InetAddress address, int port, boolean epoll, Operation<Connection> original) {
        AsyncServerPinger.Ping ping = AsyncServerPinger.CURRENT.get();
        return ping != null ? ping.connection : original.call(address, port, epoll);
    }
}
