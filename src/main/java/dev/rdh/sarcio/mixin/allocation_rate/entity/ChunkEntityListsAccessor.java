package dev.rdh.sarcio.mixin.allocation_rate.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Chunk.class)
public interface ChunkEntityListsAccessor {
	@Accessor("entityLists")
	ClassInheritanceMultiMap<Entity>[] sarcio$getEntityLists();
}
