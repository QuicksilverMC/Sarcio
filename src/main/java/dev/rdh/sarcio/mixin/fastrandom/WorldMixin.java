package dev.rdh.sarcio.mixin.fastrandom;

import java.util.Random;
import dev.rdh.sarcio.util.Xoshiro256StarStarRandom;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
abstract class WorldMixin {
	@Redirect(method = "<init>*", at = @At(value = "NEW", target = "java/util/Random", ordinal = 1))
	private Random sarcio$fastRandom() {
		return new Xoshiro256StarStarRandom();
	}
}
