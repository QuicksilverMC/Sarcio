package dev.rdh.sarcio.mixin.allocation_rate.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
abstract class EntityMixin {
	@Shadow public World world;

	@Unique private long sarcio$floatBrightnessTick = Long.MIN_VALUE;
	@Unique private float sarcio$floatBrightnessValue;

	@WrapMethod(method = "getBrightness")
	private float sarcio$cacheFloatBrightness(float partialTicks, Operation<Float> original) {
		if (this.world == null) {
			return original.call(partialTicks);
		}

		long tick = this.world.getTime();
		if (this.sarcio$floatBrightnessTick != tick) {
			this.sarcio$floatBrightnessValue = original.call(partialTicks);
			this.sarcio$floatBrightnessTick = tick;
		}
		return this.sarcio$floatBrightnessValue;
	}
}
