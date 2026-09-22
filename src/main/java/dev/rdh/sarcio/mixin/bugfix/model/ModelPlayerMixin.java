package dev.rdh.sarcio.mixin.bugfix.model;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelPlayer.class)
public class ModelPlayerMixin {
    @ModifyExpressionValue(method = "postRenderArm", at = @At(value = "CONSTANT", args = "floatValue=1.0F"))
    private float sarcio$centerSlimArmItem(float original) {
        return 0.5F;
    }
}
