package dev.rdh.sarcio.mixin.bugfix.world;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.village.SavedVillageData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Feature("bugfix.world.village_chunk_loading")
@Mixin(SavedVillageData.class)
public class SavedVillageDataMixin {
    @Shadow private World world;

    @WrapWithCondition(method = "runPendingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/village/SavedVillageData;addOrUpdateDoorsAround(Lnet/minecraft/util/math/BlockPos;)V"))
    private boolean sarcio$dontLoadChunksForDoorScan(SavedVillageData villages, BlockPos pos) {
        return this.world.isAreaLoaded(pos, 21); // 16 blocks around the position, and each door found checks for sky 5 blocks further out
    }
}
