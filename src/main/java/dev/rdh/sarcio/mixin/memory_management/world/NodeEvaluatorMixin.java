package dev.rdh.sarcio.mixin.memory_management.world;

import net.minecraft.entity.ai.pathing.NodeEvaluator;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NodeEvaluator.class)
abstract class NodeEvaluatorMixin {
	@Shadow protected WorldView world;

	@Inject(method = "done", at = @At("HEAD"))
	private void releaseBlockAccess(CallbackInfo ci) {
		this.world = null;
	}
}
