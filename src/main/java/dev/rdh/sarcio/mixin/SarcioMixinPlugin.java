package dev.rdh.sarcio.mixin;

import dev.rdh.sarcio.Asm;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Map;
import java.util.Set;

public class SarcioMixinPlugin implements IMixinConfigPlugin {
	private static final Logger LOGGER = LogManager.getLogger("Sarcio");

	// mixin names in "sarcio:disable" are relative to this, not to the package of the individual config
	private static final String ROOT_PACKAGE = "dev.rdh.sarcio.mixin";
	// shared between the per-config plugin instances so the mod list is only read (and complained about) once
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
				String resource = (ROOT_PACKAGE + '.' + name).replace('.', '/') + ".class";
				if (SarcioMixinPlugin.class.getClassLoader().getResource(resource) == null) {
					LOGGER.warn("Mod {} asked to disable mixin {}, which does not exist", id, name);
				} else {
					DISABLED.merge(name, id, (a, b) -> a + ", " + b);
				}
			}
		}
	}

	private final Set<String> reported = new ObjectOpenHashSet<>();

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		String name = mixinClassName.substring(ROOT_PACKAGE.length() + 1);
		String by = DISABLED.get(name);
		if (by != null) {
			if (this.reported.add(name)) LOGGER.info("Not applying mixin {}: disabled by {}", name, by);
			return false;
		}

		return true;
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if (mixinClassName.endsWith("core.LazySupplierMixin")) {
			Asm.asmLazyLoadBase(targetClass);
		}
	}
}
