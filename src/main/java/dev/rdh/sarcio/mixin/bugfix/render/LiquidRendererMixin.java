package dev.rdh.sarcio.mixin.bugfix.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.render.block.LiquidRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LiquidRenderer.class)
public class LiquidRendererMixin {
    @Unique
    private static final double SARCIO$SIDE_INSET = 0.001;

    @ModifyExpressionValue(method = "render", at = @At(value = "CONSTANT", args = "floatValue=0.001F"))
    private float sarcio$fixFluidGap(float original) {
        return 0.0F;
    }

    @ModifyArg(
        method = "render",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/BufferBuilder;vertex(DDD)Lnet/minecraft/client/render/vertex/BufferBuilder;"),
        slice = @Slice(
                from = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;add(III)Lnet/minecraft/util/math/BlockPos;")
        ),
        index = 0,
        // just in case
        require = 8, allow = 8
    )
    private double sarcio$insetXAgainstNeighbouringFaces(
        double x,
        double y,
        double z,
        @Local(argsOnly = true) WorldView world,
        @Local(argsOnly = true) BlockState state,
        @Local(argsOnly = true) BlockPos pos,
        @Local(ordinal = 1) BlockPos neighbour
    ) {
        if (world.isAir(neighbour)) {
            return x;
        }

        int offsetX = neighbour.getX() - pos.getX();
        if (offsetX != 0) {
            return x - offsetX * SARCIO$SIDE_INSET;
        }

        Material fluid = state.getBlock().getMaterial();
        if (x == pos.getX() && this.sarcio$isFluid(world, pos.west(), fluid)) {
            return x - SARCIO$SIDE_INSET;
        }
        if (x == pos.getX() + 1.0 && this.sarcio$isFluid(world, pos.east(), fluid)) {
            return x + SARCIO$SIDE_INSET;
        }
        return x;
    }

    @ModifyArg(
        method = "render",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/BufferBuilder;vertex(DDD)Lnet/minecraft/client/render/vertex/BufferBuilder;"),
        slice = @Slice(
                from = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;add(III)Lnet/minecraft/util/math/BlockPos;")
        ),
        index = 2,
        // just in case
        require = 8, allow = 8
    )
    private double sarcio$insetZAgainstNeighbouringFaces(
        double x,
        double y,
        double z,
        @Local(argsOnly = true) WorldView world,
        @Local(argsOnly = true) BlockState state,
        @Local(argsOnly = true) BlockPos pos,
        @Local(ordinal = 1) BlockPos neighbour
    ) {
        if (world.isAir(neighbour)) {
            return z;
        }

        if (neighbour.getX() == pos.getX()) {
            return z - (neighbour.getZ() - pos.getZ()) * SARCIO$SIDE_INSET;
        }

        Material fluid = state.getBlock().getMaterial();
        if (z == pos.getZ() && this.sarcio$isFluid(world, pos.north(), fluid)) {
            return z - SARCIO$SIDE_INSET;
        }
        if (z == pos.getZ() + 1.0 && this.sarcio$isFluid(world, pos.south(), fluid)) {
            return z + SARCIO$SIDE_INSET;
        }
        return z;
    }

    @Unique
    private boolean sarcio$isFluid(WorldView world, BlockPos pos, Material fluid) {
        return world.getBlockState(pos).getBlock().getMaterial() == fluid;
    }
}
