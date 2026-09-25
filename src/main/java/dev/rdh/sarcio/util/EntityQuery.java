package dev.rdh.sarcio.util;

import dev.rdh.sarcio.mixin.allocation_rate.entity.WorldChunkAccessor;
import dev.rdh.sarcio.mixin.allocation_rate.entity.TypeInstanceMultiMapAccessor;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFilter;
import net.minecraft.util.TypeInstanceMultiMap;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public final class EntityQuery {
	private EntityQuery() {}

	public static void addCollisionBoxes(World world, Entity subject, Box queryBox, Box collisionBox, List<Box> boxes) {
		scan(world, subject, queryBox, collisionBox, boxes);
	}

	public static void pushCollidingEntities(World world, Entity subject, Box queryBox) {
		scan(world, subject, queryBox, null, null);
	}

	private static void scan(World world, Entity subject, Box queryBox, Box collisionBox, List<Box> boxes) {
		int minChunkX = MathHelper.floor((queryBox.minX - 2.0) / 16.0);
		int maxChunkX = MathHelper.floor((queryBox.maxX + 2.0) / 16.0);
		int minChunkZ = MathHelper.floor((queryBox.minZ - 2.0) / 16.0);
		int maxChunkZ = MathHelper.floor((queryBox.maxZ + 2.0) / 16.0);

		for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
			for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
				if (!world.getChunkSource().hasChunk(chunkX, chunkZ)) {
					continue;
				}

				WorldChunk chunk = world.getChunkAt(chunkX, chunkZ);
				TypeInstanceMultiMap<Entity>[] entityLists = ((WorldChunkAccessor) chunk).sarcio$getEntities();
				int minSection = MathHelper.clamp(
					MathHelper.floor((queryBox.minY - 2.0) / 16.0), 0, entityLists.length - 1
				);
				int maxSection = MathHelper.clamp(
					MathHelper.floor((queryBox.maxY + 2.0) / 16.0), 0, entityLists.length - 1
				);

				for (int section = minSection; section <= maxSection; section++) {
					List<Entity> entities = ((TypeInstanceMultiMapAccessor) entityLists[section]).sarcio$getInstances();
					for (int i = 0; i < entities.size(); i++) {
						Entity entity = entities.get(i);
						if (entity != subject && entity.getShape().intersects(queryBox)) {
							process(subject, entity, collisionBox, boxes);
							Entity[] parts = entity.getParts();
							if (parts != null) {
								for (Entity part : parts) {
									if (part != subject && part.getShape().intersects(queryBox)) {
										process(subject, part, collisionBox, boxes);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static void process(Entity subject, Entity entity, Box collisionBox,
		List<Box> boxes) {
		if (!EntityFilter.NOT_SPECTATOR.apply(entity)) {
			return;
		}
		if (boxes == null) {
			if (entity.isPushable()) {
				entity.push(subject);
			}
			return;
		}
		if (subject.rider == entity || subject.vehicle == entity) {
			return;
		}

		Box box = entity.getCollisionShape();
		if (box != null && box.intersects(collisionBox)) {
			boxes.add(box);
		}

		box = subject.getCollisionAgainstShape(entity);
		if (box != null && box.intersects(collisionBox)) {
			boxes.add(box);
		}
	}
}
