package dev.rdh.sarcio.mixin.bugfix.world;

import java.util.Arrays;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chunk.class)
public class ChunkMixin {
    @Shadow
    @Final
    private int[] heightMap;

    @Shadow
    private int heightMapMinimum;

    @Inject(method = "setHeightMap", at = @At("TAIL"))
    private void sarcio$updateHeightMapMinimum(int[] newHeightMap, CallbackInfo ci) {
        this.heightMapMinimum = Arrays.stream(this.heightMap).min().getAsInt();
    }
}
