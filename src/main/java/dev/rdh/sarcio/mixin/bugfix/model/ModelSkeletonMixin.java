package dev.rdh.sarcio.mixin.bugfix.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModelSkeleton.class)
public abstract class ModelSkeletonMixin extends ModelBiped {
    @Override
    public void postRenderArm(float scale) {
        this.bipedRightArm.rotationPointX++;
        this.bipedRightArm.postRender(scale);
        this.bipedRightArm.rotationPointX--;
    }
}
