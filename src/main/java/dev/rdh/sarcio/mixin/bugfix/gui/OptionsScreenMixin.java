package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.options.OptionsScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Override
    public void removed() {
        this.minecraft.options.save();
    }
}
