package dev.rdh.sarcio.mixin.bugfix.gui;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.gui.screen.game.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("bugfix.gui.death_screen_clicks")
@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Shadow
    private int ticksSinceDeath;

    @Inject(method = "init()V", at = @At("HEAD"))
    private void sarcio$allowImmediateClicks(CallbackInfo ci) {
        this.ticksSinceDeath = 0;
    }
}
