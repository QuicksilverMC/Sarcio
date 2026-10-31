package dev.rdh.sarcio.mixin.opt.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rdh.sarcio.Feature;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Feature("opt.skip_client_area_check")
@Mixin(World.class)
abstract class WorldAreaCheckMixin {
	@Shadow @Final public boolean isClient;

	@WrapOperation(
		method = "tickEntity(Lnet/minecraft/entity/Entity;Z)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAreaLoaded(IIIIIIZ)Z")
	)
	private boolean skipClientAreaCheck(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean allowEmpty, Operation<Boolean> original) {
		return this.isClient || original.call(world, minX, minY, minZ, maxX, maxY, maxZ, allowEmpty);
	}
}
