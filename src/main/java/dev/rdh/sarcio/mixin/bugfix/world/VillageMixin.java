package dev.rdh.sarcio.mixin.bugfix.world;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.village.Village;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Feature("bugfix.world.village_chunk_loading")
@Mixin(Village.class)
public class VillageMixin {
    @Shadow private World world;

    @WrapOperation(method = "removeInvalidDoors", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/village/Village;isWoodenDoor(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean sarcio$dontLoadDoorChunks(Village village, BlockPos pos, Operation<Boolean> original) {
        return !this.world.isChunkLoaded(pos) || original.call(village, pos);
    }
}
