package dev.rdh.sarcio.mixin.bugfix.render.entity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EnumPlayerModelParts;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class PlayerRendererMixin extends RendererLivingEntity<AbstractClientPlayer> {
    private PlayerRendererMixin() {
        super(null, null, 0);
    }

    @Inject(method = {"renderRightArm", "renderLeftArm"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;isSneak:Z", opcode = Opcodes.PUTFIELD))
    private void sarcio$disableRidingArm(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        super.getMainModel().isRiding = false;
    }

    @Definition(id = "bipedHeadwear", field = "Lnet/minecraft/client/model/ModelPlayer;bipedHeadwear:Lnet/minecraft/client/model/ModelRenderer;")
    @Definition(id = "showModel", field = "Lnet/minecraft/client/model/ModelRenderer;showModel:Z")
    @Expression("?.bipedHeadwear.showModel = @(true)")
    @ModifyExpressionValue(method = "setModelVisibilities", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sarcio$respectHatInSpectator(boolean original, AbstractClientPlayer clientPlayer) {
        return clientPlayer.isWearing(EnumPlayerModelParts.HAT);
    }
}
