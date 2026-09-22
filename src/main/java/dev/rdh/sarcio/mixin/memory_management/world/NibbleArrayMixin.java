package dev.rdh.sarcio.mixin.memory_management.world;

import dev.rdh.sarcio.util.CompactableNibbleArray;
import java.util.Arrays;
import net.minecraft.world.chunk.NibbleArray;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NibbleArray.class)
abstract class NibbleArrayMixin implements CompactableNibbleArray {
	@Unique private static final byte[] sarcio$zero = new byte[2048];
	@Unique private static final byte[] sarcio$full = sarcio$full();

	@Shadow @Final @Mutable private byte[] data;

	@Inject(method = "<init>()V", at = @At("RETURN"))
	private void shareZeroData(CallbackInfo ci) {
		this.data = sarcio$zero;
	}

	@Inject(method = "<init>([B)V", at = @At("RETURN"))
	private void compactInitialData(byte[] data, CallbackInfo ci) {
		this.sarcio$compact();
	}

	@Inject(method = "setIndex", at = @At("HEAD"))
	private void prepareWrite(int index, int value, CallbackInfo ci) {
		this.sarcio$writableData();
	}

	@Override
	@Unique
	public byte[] sarcio$writableData() {
		if (this.data == sarcio$zero || this.data == sarcio$full) {
			this.data = this.data.clone();
		}
		return this.data;
	}

	@Override
	@Unique
	public void sarcio$compact() {
		if (this.data != sarcio$zero && Arrays.equals(this.data, sarcio$zero)) {
			this.data = sarcio$zero;
		} else if (this.data != sarcio$full && Arrays.equals(this.data, sarcio$full)) {
			this.data = sarcio$full;
		}
	}

	@Unique
	private static byte[] sarcio$full() {
		byte[] data = new byte[2048];
		Arrays.fill(data, (byte)-1);
		return data;
	}
}
