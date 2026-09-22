package dev.rdh.sarcio.mixin.memory_management.location_canon;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelResourceLocation.class)
abstract class ModelResourceLocationMixin extends ResourceLocation {
	@Shadow @Final @Mutable private String variant;
	protected ModelResourceLocationMixin(int ignored, String... parts) {
		super(ignored, parts);
	}

	@Inject(method = "<init>(Lnet/minecraft/util/ResourceLocation;Ljava/lang/String;)V", at = @At("TAIL"))
	private void reuseLocationStrings(ResourceLocation location, String ignored, CallbackInfo ci) {
		ResourceLocationAccessor self = (ResourceLocationAccessor) this;
		self.sarcio$setResourceDomain(location.getResourceDomain());
		self.sarcio$setResourcePath(location.getResourcePath());
		this.variant = this.variant.intern();
	}
}
