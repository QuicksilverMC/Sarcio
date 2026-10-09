package dev.rdh.sarcio.mixin.netty;

import dev.rdh.sarcio.util.NettyCompat;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.network.LegacyQueryHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LegacyQueryHandler.class)
public class LegacyQueryHandlerMixin {
    @Redirect(method = "channelRead", at = @At(value = "INVOKE", target = "Lio/netty/buffer/ByteBuf;readBytes(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf sarcio$readHeapCopy(ByteBuf buf, int length) {
        return NettyCompat.readHeapCopy(buf, length);
    }
}
