package dev.rdh.sarcio.mixin.bugfix.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import com.llamalad7.mixinextras.sugar.Local;
import dev.rdh.sarcio.util.CameraRayEnd;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PortalBlock;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow public abstract boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty);
    @Unique private int sarcio$range = 17;

    @WrapOperation(method = "rayTrace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;ZZZ)Lnet/minecraft/world/HitResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;canRayTrace(Lnet/minecraft/block/state/BlockState;Z)Z"))
    private boolean sarcio$cameraIgnoresGlass(Block block, BlockState state, boolean stopOnLiquid, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 1) Vec3d to) {
        if (to instanceof CameraRayEnd && sarcio$shouldIgnore(block)) return false;
        return original.call(block, state, stopOnLiquid);
    }

    @ModifyVariable(method = "rayTrace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;ZZZ)Lnet/minecraft/world/HitResult;", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private boolean sarcio$cameraIgnoresCollisionlessBlocks(boolean ignoreCollisionless, @Local(argsOnly = true, ordinal = 1) Vec3d to) {
        return ignoreCollisionless || to instanceof CameraRayEnd;
    }

    @Unique
    private boolean sarcio$shouldIgnore(Block block) {
		return block instanceof GlassBlock || block instanceof StainedGlassBlock || block instanceof PaneBlock;
	}

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

    @ModifyArg(method = "tickEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z"))
    private Collection<?> sarcio$hashRemoveAll(Collection<?> removed) {
        return removed.isEmpty() ? removed : new ObjectOpenHashSet<>(removed);
    }
}
