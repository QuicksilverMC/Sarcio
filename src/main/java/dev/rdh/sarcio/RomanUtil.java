package dev.rdh.sarcio;

public class RomanUtil {
	private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
	private static final String[] NUMERALS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

	public static String toRoman(int value) {
		StringBuilder roman = new StringBuilder();
		for (int i = 0; i < VALUES.length && value > 0; i++) {
			while (value >= VALUES[i]) {
				value -= VALUES[i];
				roman.append(NUMERALS[i]);
			}
		}

		return roman.toString();
	}
}
