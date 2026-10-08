package dev.rdh.sarcio.util;

import it.unimi.dsi.fastutil.PriorityQueue;
import it.unimi.dsi.fastutil.PriorityQueues;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import java.io.File;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.ExtremeHillsBiome;
import net.minecraft.world.biome.MesaBiome;
import net.minecraft.world.biome.MutatedBiome;
import net.minecraft.world.biome.TaigaBiome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.storage.AnvilChunkStorage;
import net.minecraft.world.chunk.storage.ChunkStorage;
import net.minecraft.world.chunk.storage.RegionIo;
import net.minecraft.world.gen.chunk.NetherChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;

public final class TerrainPrefetcher {
	public interface Holder {
		TerrainPrefetcher sarcio$terrainPrefetcher();
	}

	public interface Prefetchable {
		ChunkSource sarcio$workerCopy();

		// on a worker copy after getChunk: the random population goes on to use, if it isn't reseeded first
		Random sarcio$takeRandom();

		void sarcio$finish(WorldChunk chunk, int chunkX, int chunkZ, Random random);
	}

	private static final int WORKERS = Math.max(1, Math.min(4, Runtime.getRuntime().availableProcessors() - 2));
	private static final int WINDOW = WORKERS * 4;
	private static final ExecutorService EXECUTOR = createExecutor();

	// biomes that rewrite their own surface blocks for every column are only safe on one thread at a time
	public static final Object STATEFUL_BIOMES = new Object();

	public static final ThreadLocal<Biome[][]> BIOMES = new ThreadLocal<>();

	private final ServerChunkCache cache;
	private final ServerWorld world;
	private final File dir;
	private final Prefetchable generator;
	private final boolean sampleBiomes;
	private final PriorityQueue<ChunkSource> workerGenerators = PriorityQueues.synchronize(new ObjectArrayFIFOQueue<>());
	private int workerGeneratorCount;
	private final Long2ObjectOpenHashMap<Task> tasks = new Long2ObjectOpenHashMap<>();
	private int minZ, maxX = Integer.MIN_VALUE, maxZ, nextX, nextZ;

	private TerrainPrefetcher(ServerChunkCache cache, ServerWorld world, File dir, Prefetchable generator) {
		this.cache = cache;
		this.world = world;
		this.dir = dir;
		this.generator = generator;
		this.sampleBiomes = generator instanceof OverworldChunkGenerator;
	}

	public static TerrainPrefetcher create(ServerChunkCache cache, ServerWorld world, ChunkStorage storage, ChunkSource generator) {
		if (generator == null || !(storage instanceof AnvilChunkStorage)) {
			return null;
		}

		Class<?> type = generator.getClass();
		if (type != OverworldChunkGenerator.class && type != NetherChunkGenerator.class) {
			return null;
		}

		return new TerrainPrefetcher(cache, world, ((AnvilChunkStorage) storage).dir, (Prefetchable) generator);
	}

	public static void begin(ServerWorld world, int minX, int minZ, int maxX, int maxZ) {
		TerrainPrefetcher prefetcher = ((Holder) world.chunkCache).sarcio$terrainPrefetcher();
		if (prefetcher != null) {
			prefetcher.end();
			prefetcher.nextX = minX;
			prefetcher.nextZ = prefetcher.minZ = minZ;
			prefetcher.maxX = maxX;
			prefetcher.maxZ = maxZ;
			prefetcher.submit();
		}
	}

	public static void end(ServerWorld world) {
		TerrainPrefetcher prefetcher = ((Holder) world.chunkCache).sarcio$terrainPrefetcher();
		if (prefetcher != null) {
			prefetcher.end();
		}
	}

	private void end() {
		for (Task task : this.tasks.values()) {
			task.claimed.set(true);
		}

		this.tasks.clear();
		this.maxX = Integer.MIN_VALUE;
	}

	private void submit() {
		BiomeSource biomeSource = this.world.getBiomeSource();
		while (this.tasks.size() < WINDOW && this.nextX <= this.maxX) {
			int x = this.nextX;
			int z = this.nextZ;
			if (++this.nextZ > this.maxZ) {
				this.nextZ = this.minZ;
				this.nextX++;
			}

			if (this.cache.hasChunk(x, z) || RegionIo.getRegionFile(this.dir, x, z).hasChunkData(x & 31, z & 31)) {
				continue;
			}

			Biome[] biomes = this.sampleBiomes ? biomeSource.getBiomes(null, x * 16, z * 16, 16, 16) : null;
			Biome[] noiseBiomes = this.sampleBiomes ? biomeSource.getNoiseBiomes(null, x * 4 - 2, z * 4 - 2, 10, 10) : null;

			if (this.workerGeneratorCount < WORKERS) {
				this.workerGeneratorCount++;
				this.workerGenerators.enqueue(this.generator.sarcio$workerCopy());
			}

			Task task = new Task(() -> this.generate(x, z, noiseBiomes, biomes));
			this.tasks.put(ChunkPos.toLong(x, z), task);
			EXECUTOR.execute(task);
		}
	}

	private static boolean hasStatefulBiome(Biome[] biomes) {
		for (Biome biome : biomes) {
			if (biome instanceof ExtremeHillsBiome || biome instanceof MesaBiome || biome instanceof MutatedBiome
				|| biome instanceof TaigaBiome && ((TaigaBiome) biome).variant != 0) {
				return true;
			}
		}

		return false;
	}

	private Terrain generate(int x, int z, Biome[] noiseBiomes, Biome[] biomes) {
		// never empty: at most WORKERS tasks run at once, and there is a generator for each
		ChunkSource generator = this.workerGenerators.dequeue();
		if (biomes != null) {
			BIOMES.set(new Biome[][] {noiseBiomes, biomes});
		}

		try {
			WorldChunk chunk;
			if (biomes != null && hasStatefulBiome(biomes)) {
				synchronized (STATEFUL_BIOMES) {
					chunk = generator.getChunk(x, z);
				}
			} else {
				chunk = generator.getChunk(x, z);
			}

			return new Terrain(chunk, ((Prefetchable) generator).sarcio$takeRandom());
		} finally {
			BIOMES.remove();
			this.workerGenerators.enqueue(generator);
		}
	}

	public WorldChunk take(int x, int z) {
		Task task = this.tasks.isEmpty() ? null : this.tasks.remove(ChunkPos.toLong(x, z));
		Terrain terrain = null;
		if (task != null && !task.claimed.compareAndSet(false, true)) {
			try {
				terrain = task.get();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				// generating again on this thread reports the failure the vanilla way
			}
		}

		this.submit();
		if (terrain == null) {
			return null;
		}

		this.generator.sarcio$finish(terrain.chunk, x, z, terrain.random);
		return terrain.chunk;
	}

	private static ExecutorService createExecutor() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(WORKERS, WORKERS, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), runnable -> {
			Thread thread = new Thread(runnable, "Sarcio terrain worker");
			thread.setDaemon(true);
			return thread;
		});
		executor.allowCoreThreadTimeOut(true);
		return executor;
	}

	private static final class Terrain {
		final WorldChunk chunk;
		final Random random;

		Terrain(WorldChunk chunk, Random random) {
			this.chunk = chunk;
			this.random = random;
		}
	}

	private static final class Task extends FutureTask<Terrain> {
		final AtomicBoolean claimed = new AtomicBoolean();

		Task(Callable<Terrain> terrain) {
			super(terrain);
		}

		@Override
		public void run() {
			if (this.claimed.compareAndSet(false, true)) {
				super.run();
			}
		}
	}
}
