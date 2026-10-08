package dev.rdh.sarcio.mixin.fastrandom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.Random;
import dev.rdh.sarcio.util.Xoshiro256StarStarRandom;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
abstract class ItemMixin {
	@ModifyExpressionValue(method = "<clinit>", at = @At(value = "NEW", target = "java/util/Random"))
	private static Random sarcio$fastRandom(Random random) {
		return new Xoshiro256StarStarRandom();
	}
}
