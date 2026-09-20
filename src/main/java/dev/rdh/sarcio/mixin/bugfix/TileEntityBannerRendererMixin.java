package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TileEntityBannerRenderer.class)
public class TileEntityBannerRendererMixin {
    @WrapOperation(method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntityBanner;DDDFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTotalWorldTime()J"))
    private long sarcio$wrapBannerTime(World world, Operation<Long> original) {
        return original.call(world) % 100L;
    }
}
