package dev.rdh.sarcio.mixin.bugfix.world;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow public abstract boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty);
    @Unique private int sarcio$range = 17;
    @Unique private static final Set<Block> sarcio$ignoredBlocks = Sets.newHashSet(Blocks.GLASS, Blocks.GLASS_PANE, Blocks.STAINED_GLASS, Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS);

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

    @WrapOperation(method = "rayTrace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;ZZZ)Lnet/minecraft/world/HitResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getCollisionShape(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/BlockState;)Lnet/minecraft/util/math/Box;"))
    private Box tf$ignoreSpecialBlocks(Block instance, World world, BlockPos pos, BlockState state, Operation<Box> original) {
        return sarcio$ignoredBlocks.contains(state.getBlock()) ? null : original.call(instance, world, pos, state);
    }
}
