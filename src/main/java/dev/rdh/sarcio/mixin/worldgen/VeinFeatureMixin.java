package dev.rdh.sarcio.mixin.worldgen;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.VeinFeature;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VeinFeature.class)
abstract class VeinFeatureMixin {
	@Unique private static final BlockPos sarcio$outOfWorld = new BlockPos(0, -1, 0);

	@Shadow @Final private int size;
	@Unique private long[] sarcio$visited;
	@Unique private int sarcio$minX, sarcio$minY, sarcio$minZ, sarcio$sizeXZ, sarcio$sizeY;

	@Inject(method = "place", at = @At("HEAD"))
	private void sarcio$resetVisited(World world, Random random, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		int reachXZ = MathHelper.ceil(this.size * 3 / 16.0F + 0.5F) + 1;
		int reachY = MathHelper.ceil(this.size / 16.0F + 0.5F) + 1;
		this.sarcio$minX = pos.getX() + 8 - reachXZ;
		this.sarcio$minY = pos.getY() - 2 - reachY;
		this.sarcio$minZ = pos.getZ() + 8 - reachXZ;
		this.sarcio$sizeXZ = reachXZ * 2 + 1;
		this.sarcio$sizeY = reachY * 2 + 3;
		int words = this.sarcio$sizeXZ * this.sarcio$sizeY * this.sarcio$sizeXZ + 63 >> 6;
		if (this.sarcio$visited == null || this.sarcio$visited.length < words) {
			this.sarcio$visited = new long[words];
		} else {
			Arrays.fill(this.sarcio$visited, 0, words, 0L);
		}
	}

	@Redirect(method = "place", at = @At(value = "NEW", target = "net/minecraft/util/math/BlockPos"))
	private BlockPos sarcio$markVisited(int x, int y, int z) {
		int bx = x - this.sarcio$minX;
		int by = y - this.sarcio$minY;
		int bz = z - this.sarcio$minZ;
		if (bx >= 0 && bx < this.sarcio$sizeXZ && by >= 0 && by < this.sarcio$sizeY && bz >= 0 && bz < this.sarcio$sizeXZ) {
			int bit = (bx * this.sarcio$sizeY + by) * this.sarcio$sizeXZ + bz;
			long[] v = this.sarcio$visited;
			if ((v[bit >> 6] & 1L << bit) != 0) {
				return sarcio$outOfWorld;
			}

			v[bit >> 6] |= 1L << bit;
		}

		return new BlockPos(x, y, z);
	}
}
