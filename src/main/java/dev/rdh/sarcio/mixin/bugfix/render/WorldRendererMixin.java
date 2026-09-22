package dev.rdh.sarcio.mixin.bugfix.render;

import java.nio.IntBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private IntBuffer rawIntBuffer;

    @Shadow
    protected abstract int getBufferSize();

    @Inject(method = {"endVertex", "addVertexData"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;growBuffer(I)V"))
    private void sarcio$syncBufferPosition(CallbackInfo ci) {
        this.rawIntBuffer.clear().position(this.getBufferSize());
    }
}
