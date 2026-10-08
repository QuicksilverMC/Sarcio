package dev.rdh.sarcio.mixin.fastrandom;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.util.Random;
import dev.rdh.sarcio.util.Xoshiro256StarStarRandom;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
abstract class EntityMixin {
	@WrapOperation(method = "<init>*", at = @At(value = "NEW", target = "java/util/Random"))
	private Random sarcio$fastRandom(Operation<Random> original) {
		return new Xoshiro256StarStarRandom();
	}
}
