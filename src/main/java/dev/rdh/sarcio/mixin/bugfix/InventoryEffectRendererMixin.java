package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import dev.rdh.sarcio.RomanUtil;

@Mixin(InventoryEffectRenderer.class)
public class InventoryEffectRendererMixin {
    @ModifyArg(method = "drawActivePotionEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I", ordinal = 0), index = 0)
    private String sarcio$showHighPotionLevels(String name, @Local PotionEffect effect) {
        int level = effect.getAmplifier() + 1;
        return level > 4 ? name + " " + RomanUtil.toRoman(level) : name;
    }
}
