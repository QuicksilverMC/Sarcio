package dev.rdh.sarcio.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import net.minecraft.server.world.ScheduledTick;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.StructureBox;

public final class ChunkIndexedTickSet extends TreeSet<ScheduledTick> {
	private final Long2ObjectOpenHashMap<ObjectArrayList<ScheduledTick>> byChunk = new Long2ObjectOpenHashMap<>();

	private static long key(ScheduledTick tick) {
		return ChunkPos.toLong(tick.pos.getX() >> 4, tick.pos.getZ() >> 4);
	}

	@Override
	public boolean add(ScheduledTick tick) {
		if (!super.add(tick)) {
			return false;
		}

		long key = key(tick);
		ObjectArrayList<ScheduledTick> ticks = this.byChunk.get(key);
		if (ticks == null) {
			this.byChunk.put(key, ticks = new ObjectArrayList<>(4));
		}

		ticks.add(tick);
		return true;
	}

	@Override
	public boolean remove(Object tick) {
		if (!super.remove(tick)) {
			return false;
		}

		this.unindex((ScheduledTick) tick);
		return true;
	}

	private void unindex(ScheduledTick tick) {
		long key = key(tick);
		ObjectArrayList<ScheduledTick> ticks = this.byChunk.get(key);
		// by identity: equals only looks at the position and block
		for (int i = ticks.size() - 1; i >= 0; i--) {
			if (ticks.get(i) == tick) {
				ticks.remove(i);
				break;
			}
		}

		if (ticks.isEmpty()) {
			this.byChunk.remove(key);
		}
	}

	@Override
	public ScheduledTick pollFirst() {
		ScheduledTick tick = super.pollFirst();
		if (tick != null) {
			this.unindex(tick);
		}

		return tick;
	}

	@Override
	public ScheduledTick pollLast() {
		ScheduledTick tick = super.pollLast();
		if (tick != null) {
			this.unindex(tick);
		}

		return tick;
	}

	@Override
	public void clear() {
		super.clear();
		this.byChunk.clear();
	}

	@Override
	public Iterator<ScheduledTick> iterator() {
		Iterator<ScheduledTick> iterator = super.iterator();
		return new Iterator<ScheduledTick>() {
			private ScheduledTick last;

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public ScheduledTick next() {
				return this.last = iterator.next();
			}

			@Override
			public void remove() {
				iterator.remove();
				ChunkIndexedTickSet.this.unindex(this.last);
			}
		};
	}

	public Iterator<ScheduledTick> iterator(StructureBox bounds) {
		List<ScheduledTick> ticks = this.getTicks(bounds);
		if (ticks == null) {
			return Collections.emptyIterator();
		}

		Iterator<ScheduledTick> iterator = ticks.iterator();
		return new Iterator<ScheduledTick>() {
			private ScheduledTick last;

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public ScheduledTick next() {
				return this.last = iterator.next();
			}

			@Override
			public void remove() {
				ChunkIndexedTickSet.this.remove(this.last);
			}
		};
	}

	private List<ScheduledTick> getTicks(StructureBox bounds) {
		ObjectArrayList<ScheduledTick> result = null;
		for (int chunkX = bounds.minX >> 4; chunkX <= bounds.maxX - 1 >> 4; chunkX++) {
			for (int chunkZ = bounds.minZ >> 4; chunkZ <= bounds.maxZ - 1 >> 4; chunkZ++) {
				ObjectArrayList<ScheduledTick> ticks = this.byChunk.get(ChunkPos.toLong(chunkX, chunkZ));
				if (ticks == null) {
					continue;
				}

				for (int i = 0; i < ticks.size(); i++) {
					ScheduledTick tick = ticks.get(i);
					int x = tick.pos.getX();
					int z = tick.pos.getZ();
					if (x >= bounds.minX && x < bounds.maxX && z >= bounds.minZ && z < bounds.maxZ) {
						if (result == null) {
							result = new ObjectArrayList<>();
						}

						result.add(tick);
					}
				}
			}
		}

		if (result != null) {
			result.sort(null);
		}

		return result;
	}
}
