package dev.rdh.sarcio.mixin.memory_management.world;

import java.io.DataInputStream;
import java.io.IOException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.chunk.storage.AnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilChunkStorage.class)
abstract class AnvilChunkStorageMixin {
	@Redirect(method = "loadChunk(Lnet/minecraft/world/World;II)Lnet/minecraft/world/chunk/WorldChunk;", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtIo;read(Ljava/io/DataInputStream;)Lnet/minecraft/nbt/NbtCompound;"))
	private NbtCompound closeChunkStream(DataInputStream stream) throws IOException {
		try (stream) {
			return NbtIo.read(stream);
		}
	}
}
