package dev.rdh.sarcio.mixin.netty;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rdh.sarcio.util.NettyCompat;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PacketByteBuf.class)
public class PacketByteBufMixin {
    @WrapOperation(method = "readBytes(I)Lio/netty/buffer/ByteBuf;", at = @At(value = "INVOKE", target = "Lio/netty/buffer/ByteBuf;readBytes(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf sarcio$readHeapCopy(ByteBuf delegate, int length, Operation<ByteBuf> original) {
        return NettyCompat.readHeapCopy(delegate, length);
    }
}
