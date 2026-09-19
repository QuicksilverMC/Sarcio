package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityPistonRenderer;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TileEntityPistonRenderer.class)
public class TileEntityPistonRendererMixin {
    @WrapOperation(method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntityPiston;DDDFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;setTranslation(DDD)V", ordinal = 0))
    private void sarcio$fixPrecision(WorldRenderer instance, double origX, double origY, double origZ, Operation<Void> original, TileEntityPiston te, double x, double y, double z, float partialTicks, @Local BlockPos blockPos) {
        original.call(instance, x - (double) blockPos.getX() + (double) te.getOffsetX(partialTicks), y - (double) blockPos.getY() + (double) te.getOffsetY(partialTicks), z - (double) blockPos.getZ() + (double) te.getOffsetZ(partialTicks));
    }

    @WrapOperation(method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntityPiston;DDDFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;setTranslation(DDD)V", ordinal = 1))
    private void sarcio$fixPrecision2(WorldRenderer instance, double origX, double origY, double origZ, Operation<Void> original, TileEntityPiston te, double x, double y, double z, @Local BlockPos blockPos) {
        original.call(instance, x - (double) blockPos.getX(), y - (double) blockPos.getY(), z - (double) blockPos.getZ());
    }
}
