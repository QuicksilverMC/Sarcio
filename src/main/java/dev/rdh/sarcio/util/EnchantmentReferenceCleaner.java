package dev.rdh.sarcio.util;

public final class EnchantmentReferenceCleaner {
	private static Clearable hurtIterator;
	private static Clearable damageIterator;
	private static Clearable protectionModifier;

	private EnchantmentReferenceCleaner() {
	}

	public static void registerHurtIterator(Clearable iterator) {
		hurtIterator = iterator;
	}

	public static void registerDamageIterator(Clearable iterator) {
		damageIterator = iterator;
	}

	public static void registerProtectionModifier(Clearable modifier) {
		protectionModifier = modifier;
	}

	public static void clearProtectionModifier() {
		protectionModifier.clearReferences();
	}

	public static void clearHurtIterator() {
		hurtIterator.clearReferences();
	}

	public static void clearDamageIterator() {
		damageIterator.clearReferences();
	}

	public interface Clearable {
		void clearReferences();
	}
}
