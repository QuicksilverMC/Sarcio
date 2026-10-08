package dev.rdh.sarcio.mixin.allocation_rate.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
abstract class WorldLightingMixin {
	@Unique private static final Direction[] sarcio$facings = Direction.values();
	@Unique private final BlockPos.Mutable sarcio$neighborPosition = new BlockPos.Mutable();
	@Unique private final BlockPos.Mutable sarcio$rawLightPosition = new BlockPos.Mutable();

	@Shadow public abstract boolean hasSkyAccess(BlockPos pos);
	@Shadow public abstract BlockState getBlockState(BlockPos pos);
	@Shadow public abstract int getLight(LightType type, BlockPos pos);

	@Inject(method = "findLight", at = @At("HEAD"), cancellable = true)
	private void reuseNeighborPosition(BlockPos pos, LightType lightType, CallbackInfoReturnable<Integer> cir) {
		if (lightType == LightType.SKY && this.hasSkyAccess(pos)) {
			cir.setReturnValue(15);
			return;
		}

		Block block = this.getBlockState(pos).getBlock();
		int light = lightType == LightType.SKY ? 0 : block.getLight();
		int opacity = block.getOpacity();
		if (opacity >= 15 && block.getLight() > 0) {
			opacity = 1;
		}
		opacity = Math.max(1, opacity);

		if (opacity >= 15 || light >= 14) {
			cir.setReturnValue(opacity >= 15 ? 0 : light);
			return;
		}

		BlockPos.Mutable neighbor = this.sarcio$rawLightPosition;
		for (Direction facing : sarcio$facings) {
			neighbor.set(
				pos.getX() + facing.getOffsetX(),
				pos.getY() + facing.getOffsetY(),
				pos.getZ() + facing.getOffsetZ()
			);
			light = Math.max(light, this.getLight(lightType, neighbor) - opacity);
			if (light >= 14) {
				break;
			}
		}
		cir.setReturnValue(light);
	}

	@WrapOperation(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;up()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseUpPosition(BlockPos pos, Operation<BlockPos> original) {
		return offset(pos, Direction.UP);
	}

	@WrapOperation(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;east()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseEastPosition(BlockPos pos, Operation<BlockPos> original) {
		return offset(pos, Direction.EAST);
	}

	@WrapOperation(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;west()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseWestPosition(BlockPos pos, Operation<BlockPos> original) {
		return offset(pos, Direction.WEST);
	}

	@WrapOperation(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;south()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseSouthPosition(BlockPos pos, Operation<BlockPos> original) {
		return offset(pos, Direction.SOUTH);
	}

	@WrapOperation(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;north()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseNorthPosition(BlockPos pos, Operation<BlockPos> original) {
		return offset(pos, Direction.NORTH);
	}

	@Unique
	private BlockPos offset(BlockPos pos, Direction facing) {
		return this.sarcio$neighborPosition.set(
			pos.getX() + facing.getOffsetX(),
			pos.getY() + facing.getOffsetY(),
			pos.getZ() + facing.getOffsetZ()
		);
	}
}
