package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.ModelVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelVillager.class)
public class ModelVillagerMixin {
    @ModifyExpressionValue(method = "<init>(FFII)V", at = @At(value = "CONSTANT", args = "intValue=18"))
    private int tf$fixVillagerRobeHeight(int original) {
        return 20;
    }
}
