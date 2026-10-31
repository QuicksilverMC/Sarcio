package dev.rdh.sarcio.mixin.opt.world;

import dev.rdh.sarcio.Feature;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Feature("opt.fast_entity_unload")
@Mixin(World.class)
abstract class WorldMixin {
	@SuppressWarnings("SuspiciousMethodCalls")
	@Redirect(method = "tickEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z"))
	private boolean sarcio$hashRemoveAll(List<?> list, Collection<?> removed) {
		return !removed.isEmpty() && list.removeAll(new ObjectOpenHashSet<>(removed));
	}
}
