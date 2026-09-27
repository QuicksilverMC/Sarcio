package dev.rdh.sarcio.mixin.bugfix.render;

import net.minecraft.client.render.ProgressRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProgressRenderer.class)
public class ProgressRendererMixin {
    @Inject(method = "progressStagePercentage", at = @At("HEAD"), cancellable = true)
    private void sarcio$removeProgressScreen(int percentage, CallbackInfo ci) {
        if (percentage < 0) {
            ci.cancel();
        }
    }
}
