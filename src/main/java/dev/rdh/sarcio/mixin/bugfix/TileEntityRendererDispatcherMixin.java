package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

@Mixin(TileEntityRendererDispatcher.class)
public abstract class TileEntityRendererDispatcherMixin {
    @Shadow
    public abstract <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(Class<? extends TileEntity> teClass);

    @Definition(id = "te", local = @Local(type = TileEntity.class, argsOnly = true))
    @Expression("te == null")
    @ModifyExpressionValue(method = "getSpecialRenderer", at = @At("MIXINEXTRAS:EXPRESSION"))
    public boolean sarcio$dontRenderInvalidTile(boolean original, @Local(argsOnly = true) TileEntity te) {
        return original || te.isInvalid();
    }

    @Inject(method = "renderTileEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getCombinedLight(Lnet/minecraft/util/BlockPos;I)I"))
    private void sarcio$enableLighting(CallbackInfo ci) {
        RenderHelper.enableStandardItemLighting();
    }
}
