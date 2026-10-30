package dev.rdh.sarcio;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SarcioPreLaunch implements PreLaunchEntrypoint {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onPreLaunch() {
		useAsciiDigits();
	}

	// java 9+ formats numbers with non-ASCII digits in some locales, breaking paths
	private static void useAsciiDigits() {
		Locale format = Locale.getDefault(Locale.Category.FORMAT);
		if (DecimalFormatSymbols.getInstance(format).getZeroDigit() == '0') {
			return;
		}

		Locale ascii = new Locale.Builder().setLocale(format).setUnicodeLocaleKeyword("nu", "latn").build();
		Locale.setDefault(Locale.Category.FORMAT, ascii);
		LOGGER.info("Using ASCII digits instead of {} digits", format.toLanguageTag());
	}
}
