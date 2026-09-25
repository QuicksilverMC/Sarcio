package dev.rdh.sarcio.mixin.allocation_rate.entity;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeInstanceMultiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TypeInstanceMultiMap.class)
public interface TypeInstanceMultiMapAccessor {
	@Accessor("instances")
	List<Entity> sarcio$getInstances();
}
