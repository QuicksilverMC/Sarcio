package dev.rdh.sarcio.mixin.core;

import net.minecraft.util.Long2ObjectHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Long2ObjectHashMap.class)
abstract class Long2ObjectHashMapMixin {
	/**
	 * @author embeddedt
	 * @reason Use a better hash (from TMCW) that avoids collisions.
	 */
	@Overwrite
	private static int hash(long key) {
		return (int) key + (int) (key >>> 32) * 92821;
	}
}
