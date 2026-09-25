package dev.rdh.sarcio.mixin.memory_management.location_canon;

import net.minecraft.client.resource.model.ModelIdentifier;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelIdentifier.class)
abstract class ModelIdentifierMixin extends Identifier {
	@Shadow @Final @Mutable private String variant;
	protected ModelIdentifierMixin(int ignored, String... parts) {
		super(ignored, parts);
	}

	@Inject(method = "<init>(Lnet/minecraft/resource/Identifier;Ljava/lang/String;)V", at = @At("TAIL"))
	private void reuseLocationStrings(Identifier location, String ignored, CallbackInfo ci) {
		IdentifierAccessor self = (IdentifierAccessor) this;
		self.sarcio$setResourceDomain(location.getNamespace());
		self.sarcio$setResourcePath(location.getPath());
		this.variant = this.variant.intern();
	}
}
