package dev.rdh.sarcio.mixin.bugfix.render;

import dev.rdh.sarcio.util.CameraRayEnd;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @WrapWithCondition(method = "updateShader", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;loadShader(Lnet/minecraft/resource/Identifier;)V"))
    private boolean sarcio$onlyShadeFirstPerson(GameRenderer instance, Identifier shader) {
        return this.minecraft.options.perspective == 0;
    }

    @Shadow
    private Minecraft minecraft;

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getBrightness(Lnet/minecraft/util/math/BlockPos;)F"))
    private BlockPos sarcio$fixSkyDarkening(BlockPos original) {
        return new BlockPos(this.minecraft.getCamera().getEyePosition(1.0F));
    }

    @ModifyVariable(method = "updateLightMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/living/effect/StatusEffect;)Z"), ordinal = 11)
    private float sarcio$clampRedBeforeNightVision(float red) {
        return Math.min(red, 1.0F);
    }

    @ModifyVariable(method = "updateLightMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/living/effect/StatusEffect;)Z"), ordinal = 12)
    private float sarcio$clampGreenBeforeNightVision(float green) {
        return Math.min(green, 1.0F);
    }

    @ModifyVariable(method = "updateLightMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/living/effect/StatusEffect;)Z"), ordinal = 13)
    private float sarcio$clampBlueBeforeNightVision(float blue) {
        return Math.min(blue, 1.0F);
    }

    @ModifyArg(method = "transformCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;rayTrace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/world/HitResult;"), index = 1)
    private Vec3d sarcio$markCameraRay(Vec3d to) {
        return new CameraRayEnd(to);
    }
}
