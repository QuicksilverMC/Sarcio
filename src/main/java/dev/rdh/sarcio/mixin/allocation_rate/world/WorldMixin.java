package dev.rdh.sarcio.mixin.allocation_rate.world;

import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
abstract class WorldMixin {
	@Shadow @Final public boolean isClient;
	@Shadow @Final public List<PlayerEntity> players;
	@Shadow protected abstract int getChunkViewDistance();

	@Unique private int sarcio$chunkX = Integer.MIN_VALUE;
	@Unique private int sarcio$chunkZ = Integer.MIN_VALUE;
	@Unique private int sarcio$renderDistance = Integer.MIN_VALUE;
	@Unique private int[] sarcio$playerChunkXs = new int[0];
	@Unique private int[] sarcio$playerChunkZs = new int[0];
	@Unique private boolean sarcio$skipActiveChunkBuild;

	@Inject(method = "purgeTickingChunks", at = @At("HEAD"))
	private void checkActiveChunkBuild(CallbackInfo ci) {
		int renderDistance = this.getChunkViewDistance();
		if (!this.isClient) {
			int playerCount = this.players.size();
			this.sarcio$skipActiveChunkBuild = renderDistance == this.sarcio$renderDistance
				&& playerCount == this.sarcio$playerChunkXs.length;
			if (playerCount != this.sarcio$playerChunkXs.length) {
				this.sarcio$playerChunkXs = new int[playerCount];
				this.sarcio$playerChunkZs = new int[playerCount];
			}

			for (int i = 0; i < playerCount; i++) {
				PlayerEntity player = this.players.get(i);
				int chunkX = MathHelper.floor(player.x / 16.0D);
				int chunkZ = MathHelper.floor(player.z / 16.0D);
				this.sarcio$skipActiveChunkBuild &= chunkX == this.sarcio$playerChunkXs[i]
					&& chunkZ == this.sarcio$playerChunkZs[i];
				this.sarcio$playerChunkXs[i] = chunkX;
				this.sarcio$playerChunkZs[i] = chunkZ;
			}
			this.sarcio$renderDistance = renderDistance;
			return;
		}

		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null) {
			this.sarcio$skipActiveChunkBuild = false;
			return;
		}

		int chunkX = MathHelper.floor(player.x / 16.0D);
		int chunkZ = MathHelper.floor(player.z / 16.0D);
		this.sarcio$skipActiveChunkBuild = chunkX == this.sarcio$chunkX
			&& chunkZ == this.sarcio$chunkZ
			&& renderDistance == this.sarcio$renderDistance;
		this.sarcio$chunkX = chunkX;
		this.sarcio$chunkZ = chunkZ;
		this.sarcio$renderDistance = renderDistance;
	}

	@Redirect(
		method = "purgeTickingChunks",
		at = @At(value = "INVOKE", target = "Ljava/util/Set;clear()V")
	)
	private void keepActiveChunks(Set<ChunkPos> chunks) {
		if (!this.sarcio$skipActiveChunkBuild) {
			chunks.clear();
		}
	}

	@Redirect(
		method = "purgeTickingChunks",
		at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0)
	)
	private int activeChunkPlayerCount(List<PlayerEntity> players) {
		if (this.sarcio$skipActiveChunkBuild) {
			return 0;
		}
		if (!this.isClient) {
			return players.size();
		}
		return Minecraft.getInstance().player == null ? 0 : 1;
	}

	@Redirect(
		method = "purgeTickingChunks",
		at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0)
	)
	private Object useLocalPlayer(List<PlayerEntity> players, int index) {
		return this.isClient ? Minecraft.getInstance().player : players.get(index);
	}
}
