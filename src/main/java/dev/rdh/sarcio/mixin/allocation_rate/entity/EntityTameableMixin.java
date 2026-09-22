package dev.rdh.sarcio.mixin.allocation_rate.entity;

import java.util.UUID;
import net.minecraft.entity.passive.EntityTameable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTameable.class)
abstract class EntityTameableMixin {
	@Unique private String sarcio$ownerId;
	@Unique private UUID sarcio$ownerUuid;

	@Redirect(
		method = "getOwner",
		at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;")
	)
	private UUID cacheOwnerUuid(String ownerId) {
		if (ownerId.equals(this.sarcio$ownerId)) {
			return this.sarcio$ownerUuid;
		}

		this.sarcio$ownerId = ownerId;
		try {
			return this.sarcio$ownerUuid = UUID.fromString(ownerId);
		} catch (IllegalArgumentException ignored) {
			return this.sarcio$ownerUuid = null;
		}
	}
}
