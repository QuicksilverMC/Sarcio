package dev.rdh.sarcio.mixin.allocation_rate.world;

import com.google.common.collect.Lists;
import dev.rdh.sarcio.util.EntityQuery;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.entity.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.PrimedTntEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
abstract class WorldCollisionMixin {
	@Shadow public abstract WorldBorder getWorldBorder();
	@Shadow public abstract boolean isWithinBorder(WorldBorder border, Entity entity);
	@Shadow public abstract boolean isChunkLoaded(BlockPos pos);
	@Shadow public abstract BlockState getBlockState(BlockPos pos);

	/**
	 * @author rdh
	 * @reason faster collision detection
	 */
	@Overwrite
	public List<Box> getCollisions(Entity entity, Box box) {
		List<Box> boxes = Lists.newArrayList();
		int minX = MathHelper.floor(box.minX);
		int maxX = MathHelper.floor(box.maxX + 1.0);
		int minY = MathHelper.floor(box.minY);
		int maxY = MathHelper.floor(box.maxY + 1.0);
		int minZ = MathHelper.floor(box.minZ);
		int maxZ = MathHelper.floor(box.maxZ + 1.0);
		WorldBorder border = this.getWorldBorder();
		boolean wasOutside = entity.isOutsideWorldBorder();
		boolean isInside = this.isWithinBorder(border, entity);
		BlockState borderState = Blocks.STONE.defaultState();
		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int x = minX; x < maxX; x++) {
			for (int z = minZ; z < maxZ; z++) {
				if (this.isChunkLoaded(pos.set(x, 64, z))) {
					for (int y = minY - 1; y < maxY; y++) {
						pos.set(x, y, z);
						if (wasOutside && isInside) {
							entity.setOutsideWorldBorder(false);
						} else if (!wasOutside && !isInside) {
							entity.setOutsideWorldBorder(true);
						}

						BlockState state = borderState;
						if (border.contains(pos) || !isInside) {
							state = this.getBlockState(pos);
						}
						state.getBlock().addCollisions((World) (Object) this, pos, state, box, boxes, entity);
					}
				}
			}
		}

		if (!(entity instanceof PrimedTntEntity || entity instanceof FallingBlockEntity || entity instanceof ItemEntity || entity instanceof Particle)) {
			EntityQuery.addCollisionBoxes((World) (Object) this, entity, box.grown(0.25, 0.25, 0.25), box, boxes);
		}

		return boxes;
	}
}
