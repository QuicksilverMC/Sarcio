package dev.rdh.sarcio.mixin.core;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rdh.sarcio.util.NoOpReadWriteLock;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.entity.data.SyncedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SyncedData.class)
abstract class SyncedDataMixin {
	@Shadow private ReadWriteLock lock;
	@Shadow @Final private Map<Integer, SyncedData.Entry> entries;

	@Unique private final SyncedData.Entry[] sarcio$entries = new SyncedData.Entry[32];

	@Inject(method = "<init>", at = @At("TAIL"))
	private void removeMainThreadLocking(CallbackInfo ci) {
		this.lock = NoOpReadWriteLock.INSTANCE;
	}

	@Inject(method = "register", at = @At("TAIL"))
	private void indexRegisteredEntry(int id, Object value, CallbackInfo ci) {
		this.sarcio$index(id);
	}

	@Inject(method = "add", at = @At("TAIL"))
	private void indexAddedEntry(int id, int type, CallbackInfo ci) {
		this.sarcio$index(id);
	}

	@WrapOperation(method = "getEntry", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object sarcio$getIndexedEntry(Map<Integer, SyncedData.Entry> entries, Object key, Operation<Object> original, int id) {
		SyncedData.Entry entry = id >= 0 && id < this.sarcio$entries.length ? this.sarcio$entries[id] : null;
		return entry != null ? entry : original.call(entries, key);
	}

	@Unique
	private void sarcio$index(int id) {
		if (id >= 0 && id < this.sarcio$entries.length) {
			this.sarcio$entries[id] = this.entries.get(id);
		}
	}
}
