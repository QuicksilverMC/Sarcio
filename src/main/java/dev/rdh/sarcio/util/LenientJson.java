package dev.rdh.sarcio.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class LenientJson {
	private static final Pattern VALID_ESCAPE_OR_STRAY_BACKSLASH = Pattern.compile("(\\\\[\"\\\\/bfnrtu'\\n])|\\\\");
	private static final String KEEP_VALID_ESCAPE = "$1";

	public static InputStream dropUnknownEscapes(InputStream in) throws IOException {
		try (InputStream stream = in) {
			String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
			String cleaned = VALID_ESCAPE_OR_STRAY_BACKSLASH.matcher(json).replaceAll(KEEP_VALID_ESCAPE);
			return new ByteArrayInputStream(cleaned.getBytes(StandardCharsets.UTF_8));
		}
	}
}
