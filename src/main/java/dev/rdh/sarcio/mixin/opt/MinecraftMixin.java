package dev.rdh.sarcio.mixin.opt;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.rdh.sarcio.Feature;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Feature("opt.skip_world_transition_gc")
@Mixin(Minecraft.class)
abstract class MinecraftMixin {
	@WrapWithCondition(
		method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/System;gc()V")
	)
	private boolean skipWorldTransitionGc() {
		return false;
	}
}
