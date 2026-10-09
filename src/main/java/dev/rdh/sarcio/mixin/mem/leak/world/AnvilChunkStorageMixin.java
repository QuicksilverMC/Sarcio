package dev.rdh.sarcio.mixin.mem.leak.world;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.chunk.storage.AnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilChunkStorage.class)
abstract class AnvilChunkStorageMixin {
	@WrapOperation(method = "loadChunk(Lnet/minecraft/world/World;II)Lnet/minecraft/world/chunk/WorldChunk;", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtIo;read(Ljava/io/DataInputStream;)Lnet/minecraft/nbt/NbtCompound;"))
	private NbtCompound closeChunkStream(DataInputStream stream, Operation<NbtCompound> original) throws IOException {
		try (stream) {
			return original.call(stream);
		}
	}
}
