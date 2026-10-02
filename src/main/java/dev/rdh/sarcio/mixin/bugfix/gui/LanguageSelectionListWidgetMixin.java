package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.menu.options.LanguageOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LanguageOptionsScreen.LanguageSelectionListWidget.class)
public class LanguageSelectionListWidgetMixin {
    @WrapOperation(method = "entryClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;reloadResources()V"))
    private void sarcio$fixReload(Minecraft instance, Operation<Void> original) {
        instance.getLanguageManager().reload(instance.getResourceManager());
    }
}
