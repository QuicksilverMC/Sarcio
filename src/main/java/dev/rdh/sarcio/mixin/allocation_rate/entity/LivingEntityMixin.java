package dev.rdh.sarcio.mixin.allocation_rate.entity;

import dev.rdh.sarcio.util.EntityQuery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
	protected LivingEntityMixin(World world) {
		super(world);
	}

	/**
	 * @author rdh
	 * @reason faster collisions
	 */
	@Overwrite
	public void pushAwayCollidingEntities() {
		EntityQuery.pushCollidingEntities(
			this.world, this, this.getShape().grown(0.2, 0.0, 0.2)
		);
	}
}
