package dev.rdh.sarcio.mixin.opt.worldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.rdh.sarcio.Feature;
import dev.rdh.sarcio.util.TerrainPrefetcher;
import java.util.Map;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.WorldChunkSection;
import net.minecraft.world.chunk.storage.AnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Feature("opt.worldgen.chunk_read_prefetch")
@Mixin(AnvilChunkStorage.class)
abstract class AnvilChunkStorageMixin implements TerrainPrefetcher.Loader {
	@Unique private static final ThreadLocal<WorldChunkSection[]> sarcio$readSections = new ThreadLocal<>();

	@Shadow private Map<ChunkPos, NbtCompound> chunkSaveQueue;

	@Shadow
	protected abstract WorldChunk createChunkFromNbt(World world, NbtCompound nbt);

	@Override
	public boolean sarcio$isSaving(int chunkX, int chunkZ) {
		return this.chunkSaveQueue.containsKey(new ChunkPos(chunkX, chunkZ));
	}

	@Override
	public WorldChunk sarcio$readChunk(World world, NbtCompound level) {
		return this.createChunkFromNbt(world, level);
	}

	@ModifyExpressionValue(method = "loadChunk(Lnet/minecraft/world/World;II)Lnet/minecraft/world/chunk/WorldChunk;", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object sarcio$takeRead(Object queued, @Local(argsOnly = true) World world, @Local(argsOnly = true, ordinal = 0) int chunkX, @Local(argsOnly = true, ordinal = 1) int chunkZ) {
		if (queued != null || !(world instanceof ServerWorld) || !(((ServerWorld) world).chunkCache instanceof TerrainPrefetcher.Holder)) {
			return queued;
		}

		TerrainPrefetcher prefetcher = ((TerrainPrefetcher.Holder) ((ServerWorld) world).chunkCache).sarcio$terrainPrefetcher();
		TerrainPrefetcher.Prefetched read = prefetcher == null ? null : prefetcher.takeRead(chunkX, chunkZ);
		if (read == null) {
			return null;
		}

		sarcio$readSections.set(read.sections);
		return read.nbt;
	}

	@ModifyExpressionValue(method = "createChunkFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;getList(Ljava/lang/String;I)Lnet/minecraft/nbt/NbtList;", ordinal = 0))
	private NbtList sarcio$skipReadSections(NbtList sections) {
		return sarcio$readSections.get() != null ? new NbtList() : sections;
	}

	@WrapOperation(method = "createChunkFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;setSections([Lnet/minecraft/world/chunk/WorldChunkSection;)V"))
	private void sarcio$useReadSections(WorldChunk chunk, WorldChunkSection[] sections, Operation<Void> original) {
		WorldChunkSection[] read = sarcio$readSections.get();
		sarcio$readSections.remove();
		original.call(chunk, read != null ? read : sections);
	}
}
