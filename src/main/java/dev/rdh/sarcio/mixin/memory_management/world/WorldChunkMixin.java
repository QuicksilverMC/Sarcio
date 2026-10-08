package dev.rdh.sarcio.mixin.memory_management.world;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rdh.sarcio.util.CompactableNibbleArray;
import net.minecraft.world.chunk.ChunkNibbleStorage;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.WorldChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldChunk.class)
abstract class WorldChunkMixin {
	@Shadow public abstract WorldChunkSection[] getSections();

	@WrapOperation(
		method = "update",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkNibbleStorage;getData()[B")
	)
	private byte[] getWritableLightData(ChunkNibbleStorage light, Operation<byte[]> original) {
		((CompactableNibbleArray)light).sarcio$writableData();
		return original.call(light);
	}

	@Inject(method = "update", at = @At("RETURN"))
	private void compactLightData(byte[] data, int mask, boolean fullChunk, CallbackInfo ci) {
		for (WorldChunkSection section : this.getSections()) {
			if (section != null) {
				((CompactableNibbleArray)section.getBlockLightStorage()).sarcio$compact();
				if (section.getSkyLightStorage() != null) {
					((CompactableNibbleArray)section.getSkyLightStorage()).sarcio$compact();
				}
			}
		}
	}
}
