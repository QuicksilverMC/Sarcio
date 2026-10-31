package dev.rdh.sarcio.mixin.bugfix.render.entity;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.render.entity.layer.StuckArrowLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Feature("bugfix.render.stuck_arrow_lighting")
@Mixin(StuckArrowLayer.class)
public class StuckArrowLayerMixin {
    @WrapWithCondition(method = "render", at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/Lighting;turnOff()V"), @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/Lighting;turnOn()V")})
    private boolean tf$fixArrowLighting() {
        return false;
    }
}
