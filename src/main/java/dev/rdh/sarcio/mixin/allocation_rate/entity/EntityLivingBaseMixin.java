package dev.rdh.sarcio.mixin.allocation_rate.entity;

import dev.rdh.sarcio.util.EntityQuery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityLivingBase.class)
abstract class EntityLivingBaseMixin extends Entity {
	protected EntityLivingBaseMixin(World world) {
		super(world);
	}

	@Overwrite
	public void collideWithNearbyEntities() {
		EntityQuery.pushCollidingEntities(
			this.worldObj, this, this.getEntityBoundingBox().expand(0.2, 0.0, 0.2)
		);
	}
}
