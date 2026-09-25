package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.menu.resourcepacks.ResourcePacksScreen;
import net.minecraft.client.resource.pack.ResourcePack;
import net.minecraft.client.resource.pack.ResourcePacks;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePacksScreen.class)
public class ResourcePacksScreenMixin {
    @ModifyExpressionValue(method = "render", at = @At(value = "CONSTANT", args = "intValue=77"))
    private int sarcio$moveFolderInfo(int original) {
        return 102;
    }

    @Inject(method = "buttonClicked", at = @At(value = "INVOKE", target = "Ljava/util/Collections;reverse(Ljava/util/List;)V", remap = false))
    private void sarcio$closeUnusedPacks(CallbackInfo ci) {
        ResourcePacks repository = Minecraft.getInstance().getResourcePacks();
        ResourcePack applied = repository.getServerResourcePack();
        for (ResourcePacks.UnopenedPack entry : repository.getSelectedPacks()) {
            if (applied == null || !entry.getName().equals(applied.getName())) {
                entry.close();
            }
        }
    }
}
