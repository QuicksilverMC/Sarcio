package dev.rdh.sarcio.mixin.bugfix.gui;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.render.Window;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameGui.class)
public class GameGuiMixin {
    @Feature("bugfix.gui.lenient_riding_hunger")
    @ModifyVariable(method = "renderStatusBars", at = @At("STORE"), ordinal = 0)
    private Entity showMyHunger(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return null;
        }
        return entity;
    }

    @Feature("bugfix.gui.opaque_scoreboard_text")
    @ModifyExpressionValue(method = "renderScoreboardObjective", at = @At(value = "CONSTANT", args = "intValue=553648127"))
    private int sarcio$opaqueScoreboardText(int original) {
        return -1;
    }

    @Feature("bugfix.gui.spectator_pumpkin_overlay")
    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GameGui;renderPumpkinOverlay(Lnet/minecraft/client/render/Window;)V"))
    private boolean sarcio$removeOverlayInSpectator(GameGui instance, Window scaledRes) {
        return !Minecraft.getInstance().player.isSpectator();
    }
}
