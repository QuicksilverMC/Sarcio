package dev.rdh.sarcio.mixin.bugfix;

import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiOptions.class)
public abstract class GuiOptionsMixin extends GuiScreen {
    @Override
    public void onGuiClosed() {
        this.mc.gameSettings.saveOptions();
    }
}
