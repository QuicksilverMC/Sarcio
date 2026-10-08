package dev.rdh.sarcio.mixin.allocation_rate.render;

import java.util.Collection;
import java.util.Set;
import net.minecraft.client.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ParticleManager.class)
abstract class ParticleManagerMixin {
	@ModifyArg(
		method = "tickParticles(Ljava/util/List;)V",
		at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z")
	)
	private Collection<?> sarcio$fastRemoveDead(Collection<?> dead) {
		return dead.isEmpty() ? dead : Set.copyOf(dead);
	}
}
