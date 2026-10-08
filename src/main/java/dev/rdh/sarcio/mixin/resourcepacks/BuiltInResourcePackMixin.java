package dev.rdh.sarcio.mixin.resourcepacks;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.io.File;
import net.minecraft.client.resource.pack.BuiltInResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BuiltInResourcePack.class)
abstract class BuiltInResourcePackMixin {
	@WrapOperation(
		method = "getAsset",
		at = @At(value = "INVOKE", target = "Ljava/io/File;isFile()Z")
	)
	private boolean trustValidatedResourceIndex(File file, Operation<Boolean> original) {
		return true;
	}
}
