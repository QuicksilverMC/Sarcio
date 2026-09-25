package dev.rdh.sarcio.mixin.bugfix.render.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.WitherRenderer;
import net.minecraft.entity.living.mob.monster.boss.WitherEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherRenderer.class)
public abstract class WitherRendererMixin extends MobRenderer<WitherEntity> {
    private WitherRendererMixin() {
        super(null, null, 0);
    }

    @Inject(method = "applyScale(Lnet/minecraft/entity/living/mob/monster/boss/WitherEntity;F)V", at = @At("RETURN"))
    private void sarcio$setWitherShadowScale(WitherEntity entitylivingbaseIn, float partialTickTime, CallbackInfo ci, @Local(ordinal = 1) float f) {
        this.shadowSize = Math.abs(f);
    }
}
