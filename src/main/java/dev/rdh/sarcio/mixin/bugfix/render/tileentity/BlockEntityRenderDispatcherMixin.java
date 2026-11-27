package dev.rdh.sarcio.mixin.bugfix.render.tileentity;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.platform.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @Shadow
    public abstract <T extends BlockEntity> BlockEntityRenderer<T> getRenderer(Class<? extends BlockEntity> teClass);

    @Feature("bugfix.render.removed_block_entities")
    @Definition(id = "te", local = @Local(type = BlockEntity.class, argsOnly = true))
    @Expression("te == null")
    @ModifyExpressionValue(method = "getRenderer(Lnet/minecraft/block/entity/BlockEntity;)Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;", at = @At("MIXINEXTRAS:EXPRESSION"))
    public boolean sarcio$dontRenderInvalidTile(boolean original, @Local(argsOnly = true) BlockEntity te) {
        return original || te.isRemoved();
    }

    @Feature("bugfix.render.block_entity_lighting")
    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getLightColor(Lnet/minecraft/util/math/BlockPos;I)I"))
    private void sarcio$enableLighting(CallbackInfo ci) {
        // Lighting.turnOn sets redundant state which is slow
        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
        GlStateManager.shadeModel(GL11.GL_FLAT);
    }
}
