package dev.rdh.sarcio.mixin.bugfix.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
public class WorldHorizonMixin {
    @ModifyExpressionValue(method = "getHorizonHeight", at = @At(value = "CONSTANT", args = "doubleValue=63.0"))
    private double sarcio$horizonAtVoid(double original) {
        return 0.0D;
    }
}
