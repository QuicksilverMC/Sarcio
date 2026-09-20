package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.BlockPos;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @WrapWithCondition(method = "loadEntityShader", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;loadShader(Lnet/minecraft/util/ResourceLocation;)V"))
    private boolean sarcio$onlyShadeFirstPerson(EntityRenderer instance, ResourceLocation shader) {
        return this.mc.gameSettings.thirdPersonView == 0;
    }

    @Shadow
    private Minecraft mc;

    @ModifyArg(method = "updateRenderer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getLightBrightness(Lnet/minecraft/util/BlockPos;)F"))
    private BlockPos sarcio$fixSkyDarkening(BlockPos original) {
        return new BlockPos(this.mc.getRenderViewEntity().getPositionEyes(1.0F));
    }

    @Inject(method = "updateLightmap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    private void sarcio$clampBeforeNightVision(float partialTicks, CallbackInfo ci, @Local(ordinal = 11) LocalFloatRef red, @Local(ordinal = 12) LocalFloatRef green, @Local(ordinal = 13) LocalFloatRef blue) {
        red.set(Math.min(red.get(), 1.0F));
        green.set(Math.min(green.get(), 1.0F));
        blue.set(Math.min(blue.get(), 1.0F));
    }
}
