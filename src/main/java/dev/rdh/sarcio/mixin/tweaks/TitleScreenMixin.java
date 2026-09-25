package dev.rdh.sarcio.mixin.tweaks;

import dev.rdh.sarcio.SarcioMod;
import net.minecraft.client.gui.screen.menu.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Shadow
    private ButtonWidget realmsButton;

    @Shadow
    private boolean triedRealmsInit;

    @Inject(method = "init()V", at = @At("HEAD"))
    private void sarcio$disableRealms(CallbackInfo ci) {
        this.triedRealmsInit = SarcioMod.CONFIG.disableRealms;
    }

    @Inject(method = "initWidgetsNormal", at = @At(value = "RETURN"))
    private void sarcio$disableRealmsAgain(int i, int j, CallbackInfo ci) {
        this.realmsButton.visible = !SarcioMod.CONFIG.disableRealms;
    }
}
