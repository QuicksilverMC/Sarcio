package dev.rdh.sarcio.mixin.bugfix;

import java.io.File;

import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePackRepository.class)
public class ResourcePackRepositoryMixin {
    @Shadow
    @Final
    private File dirServerResourcepacks;

    @Inject(method = "deleteOldServerResourcesPacks", at = @At("HEAD"))
    private void sarcio$createMissingDirectory(CallbackInfo ci) {
        if (!this.dirServerResourcepacks.isDirectory()) {
            this.dirServerResourcepacks.mkdirs();
        }
    }
}
