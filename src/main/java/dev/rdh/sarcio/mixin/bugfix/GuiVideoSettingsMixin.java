package dev.rdh.sarcio.mixin.bugfix;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiVideoSettings.class)
public abstract class GuiVideoSettingsMixin extends GuiScreen {
    @Unique
    private int sarcio$mipmapLevelsOnOpen;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void sarcio$rememberMipmapLevels(CallbackInfo ci) {
        this.sarcio$mipmapLevelsOnOpen = this.mc.gameSettings.mipmapLevels;
    }

    @Override
    public void onGuiClosed() {
        if (this.mc.gameSettings.mipmapLevels != this.sarcio$mipmapLevelsOnOpen) {
            this.mc.scheduleResourcesRefresh();
        }
    }
}
