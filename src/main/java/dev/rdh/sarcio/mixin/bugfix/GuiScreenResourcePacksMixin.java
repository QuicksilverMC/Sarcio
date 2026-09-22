package dev.rdh.sarcio.mixin.bugfix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenResourcePacks.class)
public class GuiScreenResourcePacksMixin {
    @ModifyExpressionValue(method = "drawScreen", at = @At(value = "CONSTANT", args = "intValue=77"))
    private int sarcio$moveFolderInfo(int original) {
        return 102;
    }

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Ljava/util/Collections;reverse(Ljava/util/List;)V", remap = false))
    private void sarcio$closeUnusedPacks(CallbackInfo ci) {
        ResourcePackRepository repository = Minecraft.getMinecraft().getResourcePackRepository();
        IResourcePack applied = repository.getResourcePackInstance();
        for (ResourcePackRepository.Entry entry : repository.getRepositoryEntries()) {
            if (applied == null || !entry.getResourcePackName().equals(applied.getPackName())) {
                entry.closeResourcePack();
            }
        }
    }
}
