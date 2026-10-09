package dev.rdh.sarcio.mixin.mem.alloc.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
abstract class EntityLightLevelMixin {
	@Shadow public World world;

	@Unique private long sarcio$brightnessTick = Long.MIN_VALUE;
	@Unique private int sarcio$brightnessValue;

	@WrapMethod(method = "getLightLevel")
	private int sarcio$cacheBrightness(float partialTicks, Operation<Integer> original) {
		if (this.world == null) {
			return original.call(partialTicks);
		}

		long tick = this.world.getTime();
		if (this.sarcio$brightnessTick != tick) {
			this.sarcio$brightnessValue = original.call(partialTicks);
			this.sarcio$brightnessTick = tick;
		}
		return this.sarcio$brightnessValue;
	}
}
