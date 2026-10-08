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

	private final Map<String, String> disabled = new Object2ObjectOpenHashMap<>();
	private final Set<String> reported = new ObjectOpenHashSet<>();
	private String mixinPackage;

	@Override
	public void onLoad(String mixinPackage) {
		this.mixinPackage = mixinPackage;
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
				String resource = (mixinPackage + '.' + name).replace('.', '/') + ".class";
				if (SarcioMixinPlugin.class.getClassLoader().getResource(resource) == null) {
					LOGGER.warn("Mod {} asked to disable mixin {}, which does not exist", id, name);
				} else {
					this.disabled.merge(name, id, (a, b) -> a + ", " + b);
				}
			}
		}
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		String name = mixinClassName.substring(this.mixinPackage.length() + 1);
		String by = this.disabled.get(name);
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
