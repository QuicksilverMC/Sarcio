package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {
    @ModifyExpressionValue(method = "renderScoreboard", at = @At(value = "CONSTANT", args = "intValue=553648127"))
    private int sarcio$opaqueScoreboardText(int original) {
        return -1;
    }

    @WrapWithCondition(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderPumpkinOverlay(Lnet/minecraft/client/gui/ScaledResolution;)V"))
    private boolean sarcio$removeOverlayInSpectator(GuiIngame instance, ScaledResolution scaledRes) {
        return !Minecraft.getMinecraft().thePlayer.isSpectator();
    }
}
