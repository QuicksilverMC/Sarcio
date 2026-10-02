package dev.rdh.sarcio.mixin.netty;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rdh.sarcio.util.NettyCompat;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.network.LegacyQueryHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LegacyQueryHandler.class)
public class LegacyQueryHandlerMixin {
    @WrapOperation(method = "channelRead", at = @At(value = "INVOKE", target = "Lio/netty/buffer/ByteBuf;readBytes(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf sarcio$readHeapCopy(ByteBuf buf, int length, Operation<ByteBuf> original) {
        return NettyCompat.readHeapCopy(buf, length);
    }
}
