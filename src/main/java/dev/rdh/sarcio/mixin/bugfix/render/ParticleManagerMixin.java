package dev.rdh.sarcio.mixin.bugfix.render;

import net.minecraft.client.ParticleManager;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @ModifyArg(method = "renderLit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/particle/Particle;render(Lnet/minecraft/client/render/vertex/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFF)V"), index = 3)
    private float sarcio$useActiveRotationX(float rotationX) {
        return Camera.dx();
    }

    @ModifyArg(method = "renderLit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/particle/Particle;render(Lnet/minecraft/client/render/vertex/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFF)V"), index = 4)
    private float sarcio$useActiveRotationZ(float rotationZ) {
        return Camera.dy();
    }

    @ModifyArg(method = "renderLit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/particle/Particle;render(Lnet/minecraft/client/render/vertex/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFF)V"), index = 5)
    private float sarcio$useActiveRotationYZ(float rotationYZ) {
        return Camera.dz();
    }

    @ModifyArg(method = "renderLit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/particle/Particle;render(Lnet/minecraft/client/render/vertex/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFF)V"), index = 6)
    private float sarcio$useActiveRotationXY(float rotationXY) {
        return Camera.forwards();
    }

    @ModifyArg(method = "renderLit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/particle/Particle;render(Lnet/minecraft/client/render/vertex/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFF)V"), index = 7)
    private float sarcio$useActiveRotationXZ(float rotationXZ) {
        return Camera.sideways();
    }
}
