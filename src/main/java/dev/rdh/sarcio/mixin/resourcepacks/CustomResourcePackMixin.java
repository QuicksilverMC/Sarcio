package dev.rdh.sarcio.mixin.resourcepacks;

import net.minecraft.client.resource.pack.CustomResourcePack;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CustomResourcePack.class)
abstract class CustomResourcePackMixin {
	/**
	 * @author embeddedt
	 * @reason reduce method size & possibly allocation rate
	 */
	@Overwrite
	private static String getPathToResource(Identifier location) {
		return "assets/" + location.getNamespace() + '/' + location.getPath();
	}
}
