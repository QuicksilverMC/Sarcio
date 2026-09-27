package dev.rdh.sarcio.mixin.bugfix.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.Identifier;
import net.minecraft.util.math.BlockPos;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "updateLightMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/living/effect/StatusEffect;)Z"))
    private void sarcio$clampBeforeNightVision(float partialTicks, CallbackInfo ci, @Local(ordinal = 11) LocalFloatRef red, @Local(ordinal = 12) LocalFloatRef green, @Local(ordinal = 13) LocalFloatRef blue) {
        red.set(Math.min(red.get(), 1.0F));
        green.set(Math.min(green.get(), 1.0F));
        blue.set(Math.min(blue.get(), 1.0F));
    }
    @WrapOperation(method = "transformCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;rayTrace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/world/HitResult;"))
    private HitResult tf$ignoreCollisionlessBlocks(ClientWorld instance, Vec3d vec3d1, Vec3d vec3d2, Operation<HitResult> original) {
        return instance.rayTrace(vec3d1, vec3d2, false, true, true);
    }
}
