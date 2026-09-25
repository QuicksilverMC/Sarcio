package dev.rdh.sarcio.mixin.bugfix.render.tileentity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.platform.Lighting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @Shadow
    public abstract <T extends BlockEntity> BlockEntityRenderer<T> getRenderer(Class<? extends BlockEntity> teClass);

    @Definition(id = "te", local = @Local(type = BlockEntity.class, argsOnly = true))
    @Expression("te == null")
    @ModifyExpressionValue(method = "getRenderer(Lnet/minecraft/block/entity/BlockEntity;)Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;", at = @At("MIXINEXTRAS:EXPRESSION"))
    public boolean sarcio$dontRenderInvalidTile(boolean original, @Local(argsOnly = true) BlockEntity te) {
        return original || te.isRemoved();
    }

    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getLightColor(Lnet/minecraft/util/math/BlockPos;I)I"))
    private void sarcio$enableLighting(CallbackInfo ci) {
        Lighting.turnOn();
    }
}
