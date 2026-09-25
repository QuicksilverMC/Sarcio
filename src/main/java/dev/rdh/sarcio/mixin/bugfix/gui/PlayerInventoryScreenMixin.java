package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import dev.rdh.sarcio.util.RomanUtil;
import net.minecraft.client.gui.screen.game.inventory.PlayerInventoryScreen;
import net.minecraft.entity.living.effect.StatusEffectInstance;

@Mixin(PlayerInventoryScreen.class)
public class PlayerInventoryScreenMixin {
    @ModifyArg(method = "drawStatusEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I", ordinal = 0), index = 0)
    private String sarcio$showHighPotionLevels(String name, @Local StatusEffectInstance effect) {
        int level = effect.getAmplifier() + 1;
        return level > 4 ? name + " " + RomanUtil.toRoman(level) : name;
    }
}
