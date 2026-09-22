package dev.rdh.sarcio.mixin.memory_management.location_canon;

import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ResourceLocation.class)
interface ResourceLocationAccessor {
	@Mutable
	@Accessor("resourceDomain")
	void sarcio$setResourceDomain(String domain);

	@Mutable
	@Accessor("resourcePath")
	void sarcio$setResourcePath(String path);
}
