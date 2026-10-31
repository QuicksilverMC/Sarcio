package dev.rdh.sarcio;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public final class Features {
	private static final Logger LOGGER = LogManager.getLogger("Sarcio");
	private static final Map<String, String> DISABLED = new Object2ObjectOpenHashMap<>();

	static {
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			CustomValue value = mod.getMetadata().getCustomValue("sarcio:disable");
			if (value == null) continue;
			String id = mod.getMetadata().getId();
			if (value.getType() != CustomValue.CvType.ARRAY) {
				LOGGER.warn("\"sarcio:disable\" of mod {} is not an array, ignoring it", id);
				continue;
			}
			for (CustomValue entry : value.getAsArray()) {
				String name = entry.getType() == CustomValue.CvType.STRING ? entry.getAsString() : String.valueOf(entry);
				DISABLED.merge(name, id, (a, b) -> a + ", " + b);
			}
		}
	}

	private Features() {}

	public static boolean anyDisabled() {
		return !DISABLED.isEmpty();
	}

	public static boolean isEnabled(String feature) {
		return !DISABLED.containsKey(feature);
	}

	// the mods disabling a feature, or null if it's enabled
	public static String disabledBy(String feature) {
		return DISABLED.get(feature);
	}
}
