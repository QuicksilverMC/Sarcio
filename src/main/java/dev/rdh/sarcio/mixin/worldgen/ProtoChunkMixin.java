package dev.rdh.sarcio.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.state.BlockState;
import net.minecraft.world.gen.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProtoChunk.class)
abstract class ProtoChunkMixin {
	@Shadow @Final private BlockState defaultBlockState;
	@Unique private final BlockState[] sarcio$states = sarcio$newStates();

	// in its own method so the constant below doesn't shrink it too
	@Unique
	private static BlockState[] sarcio$newStates() {
		return new BlockState[65536];
	}

	@ModifyExpressionValue(method = "<init>", at = @At(value = "CONSTANT", args = "intValue=65536"))
	private int sarcio$skipIdArray(int size) {
		return 0;
	}

	/**
	 * @author rdh
	 * @reason store states directly instead of round-tripping every access through the id registry
	 */
	@Overwrite
	public BlockState getBlockState(int index) {
		BlockState state = this.sarcio$states[index];
		return state != null ? state : this.defaultBlockState;
	}

	/**
	 * @author rdh
	 * @reason store states directly instead of round-tripping every access through the id registry
	 */
	@Overwrite
	public void setBlockState(int index, BlockState state) {
		this.sarcio$states[index] = state;
	}
}
