package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.overlay.PlayerTabOverlay;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @ModifyVariable(method = "render", at = @At("STORE"))
    private PlayerEntity sarcio$replaceNullPlayer(PlayerEntity entityPlayer) {
        return entityPlayer != null ? entityPlayer : Minecraft.getInstance().player;
    }
}
