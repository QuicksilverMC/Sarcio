package dev.rdh.sarcio.mixin.bugfix.render;

import java.nio.IntBuffer;
import net.minecraft.client.render.vertex.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferBuilder.class)
public abstract class BufferBuilderMixin {
    @Shadow
    private IntBuffer intBuffer;

    @Shadow
    protected abstract int getBufferIndex();

    @Inject(method = {"nextVertex", "vertices"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/BufferBuilder;grow(I)V"))
    private void sarcio$syncBufferPosition(CallbackInfo ci) {
        this.intBuffer.clear().position(this.getBufferIndex());
    }
}
