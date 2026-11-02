package dev.rdh.sarcio.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rdh.sarcio.Feature;
import dev.rdh.sarcio.SarcioMod;
import dev.rdh.sarcio.util.AsyncServerPinger;
import io.netty.channel.ChannelFuture;
import net.minecraft.network.Connection;

import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {
    @Feature("tweaks.async_server_ping")
    @WrapOperation(method = "connect", at = @At(value = "INVOKE", target = "Lio/netty/channel/ChannelFuture;syncUninterruptibly()Lio/netty/channel/ChannelFuture;"))
    private static ChannelFuture sarcio$dontWaitForPing(ChannelFuture future, Operation<ChannelFuture> original) {
        AsyncServerPinger.Ping ping = AsyncServerPinger.CURRENT.get();
        if (ping == null) {
            return original.call(future);
        }
        ping.connecting = future;
        return future;
    }

    @Feature("tweaks.log_server_disconnect")
    @Inject(method = "exceptionCaught", at = @At("HEAD"))
    private void sarcio$handleExceptionCaught(ChannelHandlerContext ctx, Throwable throwable, CallbackInfo ci) {
        if (SarcioMod.CONFIG.logServerDisconnect) {
            SarcioMod.LOGGER.warn("Exception caught while connected to {}", ctx.channel().remoteAddress(), throwable);
        }
    }
}
