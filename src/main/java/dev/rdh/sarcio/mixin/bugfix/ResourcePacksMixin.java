package dev.rdh.sarcio.mixin.bugfix;

import java.io.File;
import net.minecraft.client.resource.pack.ResourcePacks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePacks.class)
public class ResourcePacksMixin {
    @Shadow
    @Final
    private File serverPackDirectory;

    @Inject(method = "clearOldDownloads", at = @At("HEAD"))
    private void sarcio$createMissingDirectory(CallbackInfo ci) {
        if (!this.serverPackDirectory.isDirectory()) {
            this.serverPackDirectory.mkdirs();
        }
    }
}
