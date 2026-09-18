package dev.rdh.timelessfix.mixin.bugfix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiPlayerTabOverlay.class)
public class GuiPlayerTabOverlayMixin {
    @ModifyVariable(method = "renderPlayerlist", at = @At("STORE"))
    private EntityPlayer tf$replaceNullPlayer(EntityPlayer entityPlayer) {
        return entityPlayer != null ? entityPlayer : Minecraft.getMinecraft().thePlayer;
    }
}
