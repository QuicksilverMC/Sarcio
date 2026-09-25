package dev.rdh.sarcio.mixin.memory_management.location_canon;

import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Identifier.class)
interface IdentifierAccessor {
	@Mutable
	@Accessor("namespace")
	void sarcio$setResourceDomain(String domain);

	@Mutable
	@Accessor("path")
	void sarcio$setResourcePath(String path);
}
