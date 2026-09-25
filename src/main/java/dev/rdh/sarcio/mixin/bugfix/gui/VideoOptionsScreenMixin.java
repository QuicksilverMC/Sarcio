package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.options.VideoOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends Screen {
    @Unique
    private int sarcio$mipmapLevelsOnOpen;

    @Inject(method = "init()V", at = @At("TAIL"))
    private void sarcio$rememberMipmapLevels(CallbackInfo ci) {
        this.sarcio$mipmapLevelsOnOpen = this.minecraft.options.mipmapLevels;
    }

    @Override
    public void removed() {
        if (this.minecraft.options.mipmapLevels != this.sarcio$mipmapLevelsOnOpen) {
            this.minecraft.submitResourceReload();
        }
    }
}
