package dev.rdh.sarcio.mixin.core;

import dev.rdh.sarcio.util.NoOpReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.entity.data.SyncedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SyncedData.class)
abstract class SyncedDataMixin {
	@Shadow private ReadWriteLock lock;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void removeMainThreadLocking(CallbackInfo ci) {
		this.lock = NoOpReadWriteLock.INSTANCE;
	}
}
