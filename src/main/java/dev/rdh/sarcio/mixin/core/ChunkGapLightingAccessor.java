package dev.rdh.sarcio.mixin.core;

import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldChunk.class)
public interface ChunkGapLightingAccessor {
	@Accessor("recheckGaps")
	boolean sarcio$isGapLightingUpdated();
}
