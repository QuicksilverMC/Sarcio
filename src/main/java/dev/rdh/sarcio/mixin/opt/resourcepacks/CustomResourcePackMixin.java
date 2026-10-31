package dev.rdh.sarcio.mixin.opt.resourcepacks;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.resource.pack.CustomResourcePack;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Feature("opt.resourcepacks.path_concat")
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
