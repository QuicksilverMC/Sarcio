package dev.rdh.sarcio.mixin.core;

import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Chunk.class)
public interface ChunkGapLightingAccessor {
	@Accessor("isGapLightingUpdated")
	boolean sarcio$isGapLightingUpdated();
}
