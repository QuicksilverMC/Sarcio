package dev.rdh.sarcio.celeritas;

import dev.rdh.sarcio.SarcioMod;
import dev.rdh.sarcio.SarcioConfig;
import java.util.List;
import org.embeddedt.embeddium.impl.gui.framework.TextComponent;
import org.taumc.celeritas.api.OptionGUIConstructionEvent;
import org.taumc.celeritas.api.options.OptionIdentifier;
import org.taumc.celeritas.api.options.control.TickBoxControl;
import org.taumc.celeritas.api.options.structure.OptionFlag;
import org.taumc.celeritas.api.options.structure.OptionGroup;
import org.taumc.celeritas.api.options.structure.OptionImpl;
import org.taumc.celeritas.api.options.structure.OptionPage;
import org.taumc.celeritas.api.options.structure.OptionStorage;

public final class CeleritasConfigIntegration implements OptionStorage<SarcioConfig> {
	private static final CeleritasConfigIntegration INSTANCE = new CeleritasConfigIntegration();

	@Override
	public SarcioConfig getData() {
		return SarcioMod.CONFIG;
	}

	@Override
	public void save() {
		SarcioMod.CONFIG.save();
	}

	private CeleritasConfigIntegration() {
	}

	public static void register() {
		OptionGUIConstructionEvent.BUS.addListener(event -> event.addPage(createPage()));
	}

	private static OptionPage createPage() {
		OptionGroup memory = OptionGroup.createBuilder()
			.setId(id("memory"))
			.add(OptionImpl.createBuilder(boolean.class, INSTANCE)
				.setId(id("release_crash_reserve"))
				.setControl(TickBoxControl::new)
				.setBinding((config, value) -> config.releaseCrashReserve = value,
					config -> config.releaseCrashReserve)
				.setFlags(OptionFlag.REQUIRES_GAME_RESTART)
				.build())
			.add(OptionImpl.createBuilder(boolean.class, INSTANCE)
				.setId(id("disable_realms"))
				.setControl(TickBoxControl::new)
				.setBinding((config, value) -> config.disableRealms = value,
						config -> config.disableRealms)
				.build())
			.build();

		return new OptionPage(id("options"), TextComponent.translatable("sarcio.options.pages.sarcio"), List.of(memory));
	}

	private static <T> OptionIdentifier<T> id(String path) {
		return OptionIdentifier.create("sarcio", path).cast();
	}

}
