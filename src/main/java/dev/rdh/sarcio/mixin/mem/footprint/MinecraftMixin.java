package dev.rdh.sarcio.mixin.mem.footprint;

import dev.rdh.sarcio.Feature;
import dev.rdh.sarcio.util.ClassInfoManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("mem.footprint.mixin_metadata")
@Mixin(Minecraft.class)
abstract class MinecraftMixin {
	@Inject(method = "init", at = @At("RETURN"))
	private void clearMixinMetadata(CallbackInfo ci) {
		ClassInfoManager.clear();
	}
}
