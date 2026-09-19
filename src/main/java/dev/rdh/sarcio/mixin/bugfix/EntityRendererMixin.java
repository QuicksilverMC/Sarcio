package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Shadow
    private Minecraft mc;

    @WrapOperation(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;renderBlockLayer(Lnet/minecraft/util/EnumWorldBlockLayer;DILnet/minecraft/entity/Entity;)I", ordinal = 3))
    private int sarcio$offsetTranslucents(RenderGlobal instance, EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn, Operation<Integer> original) {
        GlStateManager.doPolygonOffset(-1.0F, -1.0F);
        GlStateManager.enablePolygonOffset();
        int ret = original.call(instance, blockLayerIn, partialTicks, pass, entityIn);
        GlStateManager.disablePolygonOffset();
        return ret;
    }

    @Definition(id = "getLightBrightness", method = "Lnet/minecraft/client/multiplayer/WorldClient;getLightBrightness(Lnet/minecraft/util/BlockPos;)F")
    @Expression("?.?.?.getLightBrightness(@(?))")
    @ModifyExpressionValue(method = "updateRenderer", at = @At("MIXINEXTRAS:EXPRESSION"))
    private BlockPos sarcio$fixSkyDarkening(BlockPos original) {
        return new BlockPos(this.mc.getRenderViewEntity().getPositionEyes(1.0F));
    }
}
