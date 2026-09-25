package dev.rdh.sarcio.mixin.bugfix.model;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.model.entity.VillagerModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VillagerModel.class)
public class VillagerModelMixin {
    @ModifyExpressionValue(method = "<init>(FFII)V", at = @At(value = "CONSTANT", args = "intValue=18"))
    private int sarcio$fixVillagerRobeHeight(int original) {
        return 20;
    }
}
