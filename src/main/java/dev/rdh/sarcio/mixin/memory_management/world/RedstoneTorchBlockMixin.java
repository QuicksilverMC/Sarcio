package dev.rdh.sarcio.mixin.memory_management.world;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedstoneTorchBlock.class)
abstract class RedstoneTorchBlockMixin {
	@Shadow @Mutable private static Map<World, List<?>> RECENT_TOGGLES;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void sarcio$weakenTogglesMap(CallbackInfo ci) {
		RECENT_TOGGLES = new WeakHashMap<>();
	}
}
