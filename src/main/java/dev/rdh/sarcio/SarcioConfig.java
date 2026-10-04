package dev.rdh.sarcio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SarcioConfig {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("sarcio.json");

	public boolean releaseCrashReserve;
	public boolean disableRealms;
	public boolean longChat = true;

	public static SarcioConfig load() {
		if (Files.isRegularFile(PATH)) {
			try (Reader reader = Files.newBufferedReader(PATH)) {
				SarcioConfig config = GSON.fromJson(reader, SarcioConfig.class);
				if (config != null) {
					return config;
				}
			} catch (IOException | JsonParseException exception) {
				LOGGER.warn("Could not read Sarcio config", exception);
			}
		}

		SarcioConfig config = new SarcioConfig();
		config.save();
		return config;
	}

	public void save() {
		try {
			Files.createDirectories(PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(PATH)) {
				GSON.toJson(this, writer);
			}
		} catch (IOException exception) {
			LOGGER.warn("Could not save Sarcio config", exception);
		}
	}
}
