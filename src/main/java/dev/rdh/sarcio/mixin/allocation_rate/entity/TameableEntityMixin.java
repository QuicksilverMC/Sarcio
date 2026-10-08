package dev.rdh.sarcio.mixin.allocation_rate.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.UUID;
import net.minecraft.entity.living.mob.passive.animal.tameable.TameableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TameableEntity.class)
abstract class TameableEntityMixin {
	@Unique private String sarcio$ownerId;
	@Unique private UUID sarcio$ownerUuid;

	@WrapOperation(
		method = "getOwner",
		at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;")
	)
	private UUID cacheOwnerUuid(String ownerId, Operation<UUID> original) {
		if (ownerId.equals(this.sarcio$ownerId)) {
			return this.sarcio$ownerUuid;
		}

		this.sarcio$ownerId = ownerId;
		try {
			return this.sarcio$ownerUuid = original.call(ownerId);
		} catch (IllegalArgumentException ignored) {
			return this.sarcio$ownerUuid = null;
		}
	}
}
