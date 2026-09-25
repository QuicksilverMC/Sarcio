package dev.rdh.sarcio.mixin.bugfix.render;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Shadow
    public float cameraPitch;

    @Inject(method = "prepare", at = @At("TAIL"))
    private void sarcio$flipFrontViewPitch(World worldIn, TextRenderer textRendererIn, Entity livingPlayerIn, Entity pointedEntityIn, GameOptions optionsIn, float partialTicks, CallbackInfo ci) {
        if (optionsIn.perspective == 2) {
            this.cameraPitch = -this.cameraPitch;
        }
    }
}
