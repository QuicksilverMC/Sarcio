package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Shadow
    private Minecraft mc;

    @WrapOperation(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;renderBlockLayer(Lnet/minecraft/util/EnumWorldBlockLayer;DILnet/minecraft/entity/Entity;)I", ordinal = 3))
    private int sarcio$offsetTranslucents(RenderGlobal instance, EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn, Operation<Integer> original) {
        GlStateManager.doPolygonOffset(-0.1F, -0.1F);
        GlStateManager.enablePolygonOffset();
        int ret = original.call(instance, blockLayerIn, partialTicks, pass, entityIn);
        GlStateManager.disablePolygonOffset();
        return ret;
    }

    @ModifyArg(method = "updateRenderer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getLightBrightness(Lnet/minecraft/util/BlockPos;)F"))
    private BlockPos sarcio$fixSkyDarkening(BlockPos original) {
        return new BlockPos(this.mc.getRenderViewEntity().getPositionEyes(1.0F));
    }
}
