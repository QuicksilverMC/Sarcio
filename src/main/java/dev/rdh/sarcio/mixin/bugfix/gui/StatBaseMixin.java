package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.stats.StatBase$2")
public class StatBaseMixin {
    @ModifyExpressionValue(method = "format", at = @At(value = "CONSTANT", args = "stringValue= m"))
    private String sarcio$fixMinuteUnit(String original) {
        return " min";
    }
}
