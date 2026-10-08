package dev.rdh.sarcio.mixin.fastrandom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.Random;
import dev.rdh.sarcio.util.Xoshiro256StarStarRandom;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
abstract class WorldMixin {
	@ModifyExpressionValue(method = "<init>*", at = @At(value = "NEW", target = "java/util/Random", ordinal = 1))
	private Random sarcio$fastRandom(Random random) {
		return new Xoshiro256StarStarRandom();
	}
}
