package dev.rdh.sarcio.mixin.memory_management.world;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilChunkLoader.class)
abstract class AnvilChunkLoaderMixin {
	@Redirect(method = "loadChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompressedStreamTools;read(Ljava/io/DataInputStream;)Lnet/minecraft/nbt/NBTTagCompound;"))
	private NBTTagCompound closeChunkStream(DataInputStream stream) throws IOException {
		try (stream) {
			return CompressedStreamTools.read(stream);
		}
	}
}
