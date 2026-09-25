package dev.rdh.sarcio.mixin.bugfix.render.tileentity;

import net.minecraft.client.render.block.entity.SkullRenderer;
import net.minecraft.client.render.platform.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkullRenderer.class)
public class SkullRendererMixin {
    @Inject(method = "render(FFFLnet/minecraft/util/math/Direction;FILcom/mojang/authlib/GameProfile;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/Model;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void sarcio$enableBlending(CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }
}
