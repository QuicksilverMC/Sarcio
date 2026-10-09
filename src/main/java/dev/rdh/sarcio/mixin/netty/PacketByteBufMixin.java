package dev.rdh.sarcio.mixin.netty;

import dev.rdh.sarcio.util.NettyCompat;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PacketByteBuf.class)
public class PacketByteBufMixin {
    @Redirect(method = "readBytes(I)Lio/netty/buffer/ByteBuf;", at = @At(value = "INVOKE", target = "Lio/netty/buffer/ByteBuf;readBytes(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf sarcio$readHeapCopy(ByteBuf delegate, int length) {
        return NettyCompat.readHeapCopy(delegate, length);
    }
}
