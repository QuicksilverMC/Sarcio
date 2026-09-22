package dev.rdh.sarcio.mixin.bugfix.render.tileentity;

import java.util.Map;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityBannerRenderer.class)
public class TileEntityBannerRendererMixin {
    @Unique
    private static final int SARCIO$DESIGN_LIMIT = 256;

    @Shadow
    @Final
    @Mutable
    private static Map<String, TileEntityBannerRenderer.TimedBannerTexture> DESIGNS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void sarcio$useLinkedDesignCache(CallbackInfo ci) {
        DESIGNS = new Object2ObjectLinkedOpenHashMap<>();
    }

    @WrapOperation(method = "m_09430819", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
    private Object sarcio$markDesignUsed(Map<String, TileEntityBannerRenderer.TimedBannerTexture> designs, Object pattern, Operation<Object> original) {
        return sarcio$cache(designs).getAndMoveToLast((String) pattern);
    }

    @WrapOperation(method = "m_09430819", at = @At(value = "INVOKE", target = "Ljava/util/Map;size()I", ordinal = 1))
    private int sarcio$evictLeastRecentlyUsedDesign(Map<String, TileEntityBannerRenderer.TimedBannerTexture> designs, Operation<Integer> original) {
        int size = original.call(designs);
        if (size < SARCIO$DESIGN_LIMIT) {
            return size;
        }

        TileEntityBannerRenderer.TimedBannerTexture evicted = sarcio$cache(designs).removeFirst();
        Minecraft.getMinecraft().getTextureManager().deleteTexture(evicted.bannerTexture);
        return size - 1;
    }

    @Unique
    private static Object2ObjectLinkedOpenHashMap<String, TileEntityBannerRenderer.TimedBannerTexture> sarcio$cache(
        Map<String, TileEntityBannerRenderer.TimedBannerTexture> designs
    ) {
        return (Object2ObjectLinkedOpenHashMap<String, TileEntityBannerRenderer.TimedBannerTexture>) designs;
    }

    @WrapOperation(method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntityBanner;DDDFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTotalWorldTime()J"))
    private long sarcio$wrapBannerTime(World world, Operation<Long> original) {
        return original.call(world) % 100L;
    }
}
