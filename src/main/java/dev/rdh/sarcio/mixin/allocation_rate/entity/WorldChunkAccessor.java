package dev.rdh.sarcio.mixin.allocation_rate.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.TypeInstanceMultiMap;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldChunk.class)
public interface WorldChunkAccessor {
	@Accessor("entities")
	TypeInstanceMultiMap<Entity>[] sarcio$getEntities();
}
