package dev.rdh.sarcio.mixin.opt.entity;

import dev.rdh.sarcio.Feature;
import dev.rdh.sarcio.util.NoOpReadWriteLock;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.entity.data.SyncedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SyncedData.class)
abstract class SyncedDataMixin {
	@Shadow private ReadWriteLock lock;
	@Shadow @Final private Map<Integer, SyncedData.Entry> entries;

	@Feature("opt.synced_data_array")
	@Unique private final SyncedData.Entry[] sarcio$entries = new SyncedData.Entry[32];

	@Feature("opt.synced_data_no_lock")
	@Inject(method = "<init>", at = @At("TAIL"))
	private void removeMainThreadLocking(CallbackInfo ci) {
		this.lock = NoOpReadWriteLock.INSTANCE;
	}

	@Feature("opt.synced_data_array")
	@Inject(method = "register", at = @At("TAIL"))
	private void indexRegisteredEntry(int id, Object value, CallbackInfo ci) {
		this.sarcio$index(id);
	}

	@Feature("opt.synced_data_array")
	@Inject(method = "add", at = @At("TAIL"))
	private void indexAddedEntry(int id, int type, CallbackInfo ci) {
		this.sarcio$index(id);
	}

	/**
	 * @author rdh
	 * @reason ids are 0-31, so look entries up in an array instead of the boxed map
	 */
	@Feature("opt.synced_data_array")
	@Overwrite
	private SyncedData.Entry getEntry(int id) {
		return id >= 0 && id < this.sarcio$entries.length ? this.sarcio$entries[id] : this.entries.get(id);
	}

	@Feature("opt.synced_data_array")
	@Unique
	private void sarcio$index(int id) {
		if (id >= 0 && id < this.sarcio$entries.length) {
			this.sarcio$entries[id] = this.entries.get(id);
		}
	}
}
