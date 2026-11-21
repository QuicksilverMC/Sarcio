package dev.rdh.sarcio.mixin.bugfix.render.tileentity;

import dev.rdh.sarcio.Feature;
import dev.rdh.sarcio.util.StandardLighting;
import net.minecraft.client.render.platform.Lighting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("bugfix.render.block_entity_lighting")
@Mixin(Lighting.class)
abstract class LightingMixin {
	@Inject(method = {"turnOn", "turnOff"}, at = @At("RETURN"))
	private static void sarcio$markChanged(CallbackInfo ci) {
		StandardLighting.current = false;
	}
}
