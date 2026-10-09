package dev.rdh.sarcio.mixin.allocation_rate.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
abstract class WorldLightingMixin {
	@Unique private static final Direction[] sarcio$facings = Direction.values();
	@Unique private final BlockPos.Mutable sarcio$neighborPosition = new BlockPos.Mutable();
	@Unique private final BlockPos.Mutable sarcio$rawLightPosition = new BlockPos.Mutable();

	@Redirect(
		method = "findLight",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;")
	)
	private Direction[] reuseFacings() {
		return sarcio$facings;
	}

	@Redirect(
		method = "findLight",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;offset(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseNeighborPosition(BlockPos pos, Direction facing) {
		return this.sarcio$rawLightPosition.set(
			pos.getX() + facing.getOffsetX(),
			pos.getY() + facing.getOffsetY(),
			pos.getZ() + facing.getOffsetZ()
		);
	}

	@Redirect(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;up()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseUpPosition(BlockPos pos) {
		return offset(pos, Direction.UP);
	}

	@Redirect(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;east()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseEastPosition(BlockPos pos) {
		return offset(pos, Direction.EAST);
	}

	@Redirect(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;west()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseWestPosition(BlockPos pos) {
		return offset(pos, Direction.WEST);
	}

	@Redirect(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;south()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseSouthPosition(BlockPos pos) {
		return offset(pos, Direction.SOUTH);
	}

	@Redirect(
		method = {"getRawBrightness(Lnet/minecraft/util/math/BlockPos;Z)I", "getBrightness(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;north()Lnet/minecraft/util/math/BlockPos;")
	)
	private BlockPos reuseNorthPosition(BlockPos pos) {
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
