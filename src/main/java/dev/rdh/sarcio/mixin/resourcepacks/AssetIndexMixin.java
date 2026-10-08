package dev.rdh.sarcio.mixin.resourcepacks;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.io.File;
import java.util.Map;
import net.minecraft.client.resource.AssetIndex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AssetIndex.class)
abstract class AssetIndexMixin {
	@WrapOperation(
		method = "<init>(Ljava/io/File;Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")
	)
	private Object onlyIndexExistingFiles(Map<Object, Object> map, Object key, Object value, Operation<Object> original) {
		return ((File) value).isFile() ? original.call(map, key, value) : null;
	}
}
