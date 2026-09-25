package dev.rdh.sarcio.mixin.bugfix.render.tileentity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.MovingBlockEntity;
import net.minecraft.client.render.block.entity.MovingBlockRenderer;
import net.minecraft.client.render.vertex.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MovingBlockRenderer.class)
public class MovingBlockRendererMixin {
    @WrapOperation(method = "render(Lnet/minecraft/block/entity/MovingBlockEntity;DDDFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/BufferBuilder;offset(DDD)V", ordinal = 0))
    private void sarcio$fixPrecision(BufferBuilder instance, double origX, double origY, double origZ, Operation<Void> original, MovingBlockEntity te, double x, double y, double z, float partialTicks, @Local BlockPos blockPos) {
        original.call(instance, x - (double) blockPos.getX() + (double) te.getRenderOffsetX(partialTicks), y - (double) blockPos.getY() + (double) te.getRenderOffsetY(partialTicks), z - (double) blockPos.getZ() + (double) te.getRenderOffsetZ(partialTicks));
    }

    @WrapOperation(method = "render(Lnet/minecraft/block/entity/MovingBlockEntity;DDDFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/BufferBuilder;offset(DDD)V", ordinal = 1))
    private void sarcio$fixPrecision2(BufferBuilder instance, double origX, double origY, double origZ, Operation<Void> original, MovingBlockEntity te, double x, double y, double z, @Local BlockPos blockPos) {
        original.call(instance, x - (double) blockPos.getX(), y - (double) blockPos.getY(), z - (double) blockPos.getZ());
    }
}
