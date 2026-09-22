package dev.rdh.sarcio.mixin.fastrandom;

import java.util.Random;
import dev.rdh.sarcio.util.Xoshiro256StarStarRandom;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
abstract class ItemMixin {
	@Redirect(method = "<clinit>", at = @At(value = "NEW", target = "java/util/Random"))
	private static Random sarcio$fastRandom() {
		return new Xoshiro256StarStarRandom();
	}
}
