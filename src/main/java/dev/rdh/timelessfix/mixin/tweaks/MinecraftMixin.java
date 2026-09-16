package dev.rdh.timelessfix.mixin.tweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.stream.IStream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Redirect(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderStreamIndicator(F)V"))
    private void tf$removeStreamIndicator(EntityRenderer instance, float partialTicks) {
        // No-op
    }

    @Redirect(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/stream/IStream;m_34249743()V"))
    private void tf$removeStreamCallOne(IStream instance) {
        // No-op
    }

    @Redirect(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/stream/IStream;m_06861983()V"))
    private void tf$removeStreamCallTwo(IStream instance) {
        // No-op
    }
}
