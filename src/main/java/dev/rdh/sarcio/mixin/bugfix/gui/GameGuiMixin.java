package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.render.Window;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameGui.class)
public class GameGuiMixin {
    @ModifyExpressionValue(method = "renderScoreboardObjective", at = @At(value = "CONSTANT", args = "intValue=553648127"))
    private int sarcio$opaqueScoreboardText(int original) {
        return -1;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GameGui;renderPumpkinOverlay(Lnet/minecraft/client/render/Window;)V"))
    private boolean sarcio$removeOverlayInSpectator(GameGui instance, Window scaledRes) {
        return !Minecraft.getInstance().player.isSpectator();
    }
}
