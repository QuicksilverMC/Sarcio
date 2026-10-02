package dev.rdh.sarcio.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rdh.sarcio.util.AsyncServerPinger;
import io.netty.channel.ChannelFuture;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Connection.class)
public class ConnectionMixin {
    @WrapOperation(method = "connect", at = @At(value = "INVOKE", target = "Lio/netty/channel/ChannelFuture;syncUninterruptibly()Lio/netty/channel/ChannelFuture;"))
    private static ChannelFuture sarcio$dontWaitForPing(ChannelFuture future, Operation<ChannelFuture> original) {
        AsyncServerPinger.Ping ping = AsyncServerPinger.CURRENT.get();
        if (ping == null) {
            return original.call(future);
        }
        ping.connecting = future;
        return future;
    }
}
