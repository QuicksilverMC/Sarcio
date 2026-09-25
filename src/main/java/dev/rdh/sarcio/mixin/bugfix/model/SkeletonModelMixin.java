package dev.rdh.sarcio.mixin.bugfix.model;

import net.minecraft.client.render.model.entity.HumanoidModel;
import net.minecraft.client.render.model.entity.SkeletonModel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SkeletonModel.class)
public abstract class SkeletonModelMixin extends HumanoidModel {
    @Override
    public void translateRightArm(float scale) {
        this.rightArm.x++;
        this.rightArm.transform(scale);
        this.rightArm.x--;
    }
}
