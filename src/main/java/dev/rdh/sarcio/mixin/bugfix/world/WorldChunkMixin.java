package dev.rdh.sarcio.mixin.bugfix.world;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.Iterators;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rdh.sarcio.util.WorldExt;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldChunk.class)
public class WorldChunkMixin {
    @Shadow
    @Final
    private int[] heightMap;

    @Shadow
    private int lowestHeight;

    @Shadow
    @Final
    private World world;

    @Inject(method = "setHeightMap", at = @At("TAIL"))
    private void sarcio$updateHeightMapMinimum(int[] newHeightMap, CallbackInfo ci) {
        this.lowestHeight = Arrays.stream(this.heightMap).min().getAsInt();
    }

    @WrapOperation(method = "unload", at = @At(value = "INVOKE", target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;", ordinal = 0))
    private Iterator<?> sarcio$unloadTileEntity(Collection<?> instance, Operation<Iterator<?>> original) {
        ((WorldExt) this.world).sarcio$markTileEntitiesInChunkForRemoval((WorldChunk) (Object) this);
        return Iterators.emptyIterator();
    }
}
