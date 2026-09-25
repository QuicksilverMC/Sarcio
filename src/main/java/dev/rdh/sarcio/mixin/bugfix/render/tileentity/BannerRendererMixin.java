package dev.rdh.sarcio.mixin.bugfix.render.tileentity;

import java.util.Map;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.entity.BannerRenderer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerRenderer.class)
public class BannerRendererMixin {
    @Unique
    private static final int SARCIO$DESIGN_LIMIT = 256;

    @Shadow
    @Final
    @Mutable
    private static Map<String, BannerRenderer.CachedTexture> TEXTURE_CACHE;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void sarcio$useLinkedDesignCache(CallbackInfo ci) {
        TEXTURE_CACHE = new Object2ObjectLinkedOpenHashMap<>();
    }

    @WrapOperation(method = "getTexture", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
    private Object sarcio$markDesignUsed(Map<String, BannerRenderer.CachedTexture> designs, Object pattern, Operation<Object> original) {
        return sarcio$cache(designs).getAndMoveToLast((String) pattern);
    }

    @WrapOperation(method = "getTexture", at = @At(value = "INVOKE", target = "Ljava/util/Map;size()I", ordinal = 1))
    private int sarcio$evictLeastRecentlyUsedDesign(Map<String, BannerRenderer.CachedTexture> designs, Operation<Integer> original) {
        int size = original.call(designs);
        if (size < SARCIO$DESIGN_LIMIT) {
            return size;
        }

        BannerRenderer.CachedTexture evicted = sarcio$cache(designs).removeFirst();
        Minecraft.getInstance().getTextureManager().close(evicted.texture);
        return size - 1;
    }

    @Unique
    private static Object2ObjectLinkedOpenHashMap<String, BannerRenderer.CachedTexture> sarcio$cache(
        Map<String, BannerRenderer.CachedTexture> designs
    ) {
        return (Object2ObjectLinkedOpenHashMap<String, BannerRenderer.CachedTexture>) designs;
    }

    @WrapOperation(method = "render(Lnet/minecraft/block/entity/BannerBlockEntity;DDDFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTime()J"))
    private long sarcio$wrapBannerTime(World world, Operation<Long> original) {
        return original.call(world) % 100L;
    }
}
