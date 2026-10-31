package dev.rdh.sarcio.mixin.bugfix.gui;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Feature("bugfix.gui.minute_stat_unit")
@Mixin(targets = "net.minecraft.stat.Stat$04534631")
public class StatMixin {
    @ModifyExpressionValue(method = "format", at = @At(value = "CONSTANT", args = "stringValue= m"))
    private String sarcio$fixMinuteUnit(String original) {
        return " min";
    }
}
