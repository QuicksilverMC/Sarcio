package dev.rdh.sarcio.mixin.bugfix;

import dev.rdh.sarcio.Feature;
import net.minecraft.util.LazySupplier;
import org.spongepowered.asm.mixin.Mixin;

@Feature("bugfix.thread_safe_lazy_supplier")
@Mixin(LazySupplier.class)
abstract class LazySupplierMixin<T> {
	// asmed instead
}
