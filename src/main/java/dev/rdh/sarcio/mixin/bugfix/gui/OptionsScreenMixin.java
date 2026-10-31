package dev.rdh.sarcio.mixin.bugfix.gui;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.options.OptionsScreen;
import org.spongepowered.asm.mixin.Mixin;

@Feature("bugfix.gui.save_options_on_close")
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Override
    public void removed() {
        this.minecraft.options.save();
    }
}
