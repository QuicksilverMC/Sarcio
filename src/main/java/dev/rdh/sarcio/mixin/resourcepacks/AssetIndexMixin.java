package dev.rdh.sarcio.mixin.resourcepacks;

import java.io.File;
import java.util.Map;
import net.minecraft.client.resource.AssetIndex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AssetIndex.class)
abstract class AssetIndexMixin {
	@Redirect(
		method = "<init>(Ljava/io/File;Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")
	)
	private Object onlyIndexExistingFiles(Map<Object, Object> map, Object key, Object value) {
		return ((File) value).isFile() ? map.put(key, value) : null;
	}
}
