package dev.rdh.sarcio.mixin.bugfix.render.entity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.model.PlayerModelPart;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<ClientPlayerEntity> {
    private PlayerRendererMixin() {
        super(null, null, 0);
    }

    @Inject(method = {"renderRightArm", "renderLeftArm"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/model/entity/PlayerModel;sneaking:Z", opcode = Opcodes.PUTFIELD))
    private void sarcio$disableRidingArm(ClientPlayerEntity clientPlayer, CallbackInfo ci) {
        super.getModel().riding = false;
    }

    @Definition(id = "hat", field = "Lnet/minecraft/client/render/model/entity/PlayerModel;hat:Lnet/minecraft/client/render/model/ModelPart;")
    @Definition(id = "visible", field = "Lnet/minecraft/client/render/model/ModelPart;visible:Z")
    @Expression("?.hat.visible = @(true)")
    @ModifyExpressionValue(method = "setModelStatus", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sarcio$respectHatInSpectator(boolean original, ClientPlayerEntity clientPlayer) {
        return clientPlayer.isModelPartVisible(PlayerModelPart.HAT);
    }
}
