package dev.rdh.sarcio.mixin.netty;

import dev.rdh.sarcio.util.NettyCompat;
import io.netty.buffer.ByteBuf;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.network.MultiplayerServerListPinger$14188739$47393468")
public class LegacyPingHandlerMixin {
    @Dynamic
    @Redirect(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;)V", at = @At(value = "INVOKE", target = "Lio/netty/buffer/ByteBuf;readBytes(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf sarcio$readHeapCopy(ByteBuf buf, int length) {
        return NettyCompat.readHeapCopy(buf, length);
    }
}
