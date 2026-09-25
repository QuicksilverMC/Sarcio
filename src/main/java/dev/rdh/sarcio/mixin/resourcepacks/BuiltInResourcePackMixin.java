package dev.rdh.sarcio.mixin.resourcepacks;

import java.io.File;
import net.minecraft.client.resource.pack.BuiltInResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BuiltInResourcePack.class)
abstract class BuiltInResourcePackMixin {
	@Redirect(
		method = "getAsset",
		at = @At(value = "INVOKE", target = "Ljava/io/File;isFile()Z")
	)
	private boolean trustValidatedResourceIndex(File file) {
		return true;
	}
}
