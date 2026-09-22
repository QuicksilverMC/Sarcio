package dev.rdh.sarcio.mixin.memory_management.world;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRedstoneTorch.class)
abstract class BlockRedstoneTorchMixin {
	@Shadow @Mutable private static Map<World, List<?>> toggles;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void sarcio$weakenTogglesMap(CallbackInfo ci) {
		toggles = new WeakHashMap<>();
	}
}
