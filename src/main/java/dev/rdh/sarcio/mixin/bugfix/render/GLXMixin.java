package dev.rdh.sarcio.mixin.bugfix.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import dev.rdh.sarcio.Feature;
import net.minecraft.client.render.platform.GLX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("bugfix.render.lightmap_clamp")
@Mixin(GLX.class)
abstract class GLXMixin {
    // some mods pass 255 for full brightness, which runs past the end of the lightmap
    @Inject(method = "multiTexCoord2f", at = @At("HEAD"))
    private static void sarcio$clampLightmapCoords(int unit, float u, float v, CallbackInfo ci,
                                                   @Local(argsOnly = true, ordinal = 0) LocalFloatRef clampedU,
                                                   @Local(argsOnly = true, ordinal = 1) LocalFloatRef clampedV) {
        if (unit != GLX.GL_TEXTURE1) return;
        clampedU.set(Math.clamp(u, 0.0F, 240.0F));
        clampedV.set(Math.clamp(v, 0.0F, 240.0F));
    }
}
