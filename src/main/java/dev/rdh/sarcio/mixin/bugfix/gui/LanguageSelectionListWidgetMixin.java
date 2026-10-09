package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LanguageOptionsScreen.LanguageSelectionListWidget.class)
public class LanguageSelectionListWidgetMixin {
    @Redirect(method = "entryClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;reloadResources()V"))
    private void sarcio$fixReload(Minecraft instance) {
        instance.getLanguageManager().reload(instance.getResourceManager());
    }
}
