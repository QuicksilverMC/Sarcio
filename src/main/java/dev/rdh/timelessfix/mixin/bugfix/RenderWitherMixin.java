package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.entity.boss.EntityWither;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderWither.class)
public abstract class RenderWitherMixin extends RenderLiving<EntityWither> {
    public RenderWitherMixin(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
    }

    @Inject(method = "preRenderCallback(Lnet/minecraft/entity/boss/EntityWither;F)V", at = @At("RETURN"))
    private void tf$setWitherShadowScale(EntityWither entitylivingbaseIn, float partialTickTime, CallbackInfo ci, @Local(ordinal = 1) float f) {
        this.shadowSize = Math.abs(f);
    }
}
