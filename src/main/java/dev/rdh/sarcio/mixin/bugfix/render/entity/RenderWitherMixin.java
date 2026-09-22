package dev.rdh.sarcio.mixin.bugfix.render.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.entity.boss.EntityWither;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderWither.class)
public abstract class RenderWitherMixin extends RenderLiving<EntityWither> {
    private RenderWitherMixin() {
        super(null, null, 0);
    }

    @Inject(method = "preRenderCallback(Lnet/minecraft/entity/boss/EntityWither;F)V", at = @At("RETURN"))
    private void sarcio$setWitherShadowScale(EntityWither entitylivingbaseIn, float partialTickTime, CallbackInfo ci, @Local(ordinal = 1) float f) {
        this.shadowSize = Math.abs(f);
    }
}
