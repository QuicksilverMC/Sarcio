package dev.rdh.sarcio.mixin.bugfix.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.rdh.sarcio.util.WorldExt;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(World.class)
public abstract class WorldMixin implements WorldExt {
    @Shadow
    public abstract boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty);
    @Unique
    private int sarcio$range = 17;

    @Shadow
    @Final
    public List<BlockEntity> tickingBlockEntities;
    @Shadow
    @Final
    public List<BlockEntity> blockEntities;
    @Unique
    private LongOpenHashSet sarcio$tileEntitiesChunkToBeRemoved = new LongOpenHashSet();

    @Inject(method = "updateLight", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", ordinal = 0))
    private void sarcio$updateRange(LightType lightType, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        this.sarcio$range = this.isAreaLoaded(pos, 18, false) ? 17 : 15;
    }

    @ModifyExpressionValue(method  = "updateLight", at = @At(value = "CONSTANT", args = "intValue=17", ordinal = 0))
    private int sarcio$setStaticRange(int original) {
        return 16;
    }

    @ModifyExpressionValue(method  = "updateLight", at = {@At(value = "CONSTANT", args = "intValue=17", ordinal = 1), @At(value = "CONSTANT", args = "intValue=17", ordinal = 2)})
    private int setVariableRange(int original) {
        return this.sarcio$range;
    }

    @Override
    public void sarcio$markTileEntitiesInChunkForRemoval(WorldChunk chunk) {
        if (!chunk.getBlockEntities().isEmpty()) {
            long pos = ChunkPos.toLong(chunk.chunkX, chunk.chunkZ);
            this.sarcio$tileEntitiesChunkToBeRemoved.add(pos);
        }
    }

    @Inject(method = "tickEntities", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isTickingBlockEntities:Z", opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void removeInUnloaded(CallbackInfo ci) {
        if (!this.sarcio$tileEntitiesChunkToBeRemoved.isEmpty()) {
            Predicate<BlockEntity> isInChunk = (blockEntity) -> {
                long tileChunkPos = ChunkPos.toLong(blockEntity.getPos().getX() >> 4, blockEntity.getPos().getZ() >> 4);
                return this.sarcio$tileEntitiesChunkToBeRemoved.contains(tileChunkPos);
            };
            this.tickingBlockEntities.removeIf(isInChunk);
            this.blockEntities.removeIf(isInChunk);
            this.sarcio$tileEntitiesChunkToBeRemoved.clear();
        }
    }
}
