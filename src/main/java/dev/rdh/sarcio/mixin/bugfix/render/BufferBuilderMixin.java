package dev.rdh.sarcio.mixin.bugfix.render;

import java.nio.IntBuffer;
import net.minecraft.client.render.vertex.BufferBuilder;
import net.minecraft.client.render.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferBuilder.class)
public abstract class BufferBuilderMixin {
    @Shadow
    private IntBuffer intBuffer;

    @Shadow
    protected abstract int getBufferIndex();

    @Shadow
    private VertexFormat format;

    @Inject(method = {"nextVertex", "vertices"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/BufferBuilder;grow(I)V"))
    private void sarcio$syncBufferPosition(CallbackInfo ci) {
        this.intBuffer.clear().position(this.getBufferIndex());
    }

    @ModifyArg(method = "vertices", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/BufferBuilder;grow(I)V"))
    private int sarcio$buildBuffer(int amount) {
        return amount + this.format.getVertexSize();
    }
}
