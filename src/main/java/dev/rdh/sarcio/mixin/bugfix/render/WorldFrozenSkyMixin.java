package dev.rdh.sarcio.mixin.bugfix.render;

import dev.rdh.sarcio.Feature;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Feature("bugfix.render.frozen_sky_jitter")
@Mixin(World.class)
public abstract class WorldFrozenSkyMixin {
    @Shadow @Final public boolean isClient;

    @Shadow public abstract GameRules getGameRules();

    @ModifyArg(method = "getTimeOfDay(F)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;getTimeOfDay(JF)F"))
    private float sarcio$freezeSkyTickDelta(float tickDelta) {
        return this.isClient && !this.getGameRules().getBoolean("doDaylightCycle") ? 0.0F : tickDelta;
    }
}
