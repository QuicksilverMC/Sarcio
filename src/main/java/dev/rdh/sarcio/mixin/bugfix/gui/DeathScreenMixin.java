package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.gui.screen.game.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Shadow
    private int ticksSinceDeath;

    @Inject(method = "init()V", at = @At("HEAD"))
    private void sarcio$allowImmediateClicks(CallbackInfo ci) {
        this.ticksSinceDeath = 0;
    }
}
