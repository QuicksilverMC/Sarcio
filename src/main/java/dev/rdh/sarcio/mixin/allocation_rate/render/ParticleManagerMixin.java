package dev.rdh.sarcio.mixin.allocation_rate.render;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.minecraft.client.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ParticleManager.class)
abstract class ParticleManagerMixin {
	@SuppressWarnings("SuspiciousMethodCalls")
	@Redirect(
		method = "tickParticles(Ljava/util/List;)V",
		at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z")
	)
	private boolean sarcio$fastRemoveDead(List<?> particles, Collection<?> dead) {
		return !dead.isEmpty() && particles.removeAll(Set.copyOf(dead));
	}
}
