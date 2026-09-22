package dev.rdh.sarcio.mixin.bugfix.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderManager.class)
public class RenderManagerMixin {
    @Shadow
    public float playerViewX;

    @Inject(method = "cacheActiveRenderInfo", at = @At("TAIL"))
    private void sarcio$flipFrontViewPitch(World worldIn, FontRenderer textRendererIn, Entity livingPlayerIn, Entity pointedEntityIn, GameSettings optionsIn, float partialTicks, CallbackInfo ci) {
        if (optionsIn.thirdPersonView == 2) {
            this.playerViewX = -this.playerViewX;
        }
    }
}
