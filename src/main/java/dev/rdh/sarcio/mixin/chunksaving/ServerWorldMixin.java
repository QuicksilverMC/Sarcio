package dev.rdh.sarcio.mixin.chunksaving;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.rdh.sarcio.util.ChunkIndexedTickSet;
import java.util.Iterator;
import java.util.TreeSet;
import net.minecraft.server.world.ScheduledTick;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.structure.StructureBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerWorld.class)
abstract class ServerWorldMixin {
	@ModifyExpressionValue(method = "<init>", at = @At(value = "NEW", target = "java/util/TreeSet"))
	private TreeSet<ScheduledTick> sarcio$indexTicksByChunk(TreeSet<ScheduledTick> ticks) {
		return new ChunkIndexedTickSet();
	}

	// every chunk save scanned all pending ticks in the world: hand it only the ones in the chunks it covers
	@WrapOperation(
		method = "getScheduledTicks(Lnet/minecraft/world/gen/structure/StructureBox;Z)Ljava/util/List;",
		at = @At(value = "INVOKE", target = "Ljava/util/TreeSet;iterator()Ljava/util/Iterator;")
	)
	private Iterator<ScheduledTick> sarcio$ticksInBounds(TreeSet<ScheduledTick> ticks, Operation<Iterator<ScheduledTick>> original, @Local(argsOnly = true) StructureBox bounds) {
		return ticks instanceof ChunkIndexedTickSet ? ((ChunkIndexedTickSet) ticks).iterator(bounds) : original.call(ticks);
	}
}
