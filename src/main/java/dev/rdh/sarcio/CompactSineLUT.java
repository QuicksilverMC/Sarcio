package dev.rdh.sarcio;

/**
 * originates from lithium somewhere; costs a few integer ops
 * but fits in the cache which helps a lot
 *
 * @author coderbot16, jellysquid3
 */
public final class CompactSineLUT {
	private static final int[] TABLE = new int[16384 + 1];
	private static float midpoint;

	private CompactSineLUT() {}

	public static void init(float[] sineTable) {
		for (int i = 0; i < TABLE.length; i++) {
			TABLE[i] = Float.floatToRawIntBits(sineTable[i]);
		}

		midpoint = sineTable[sineTable.length / 2];

		for (int i = 0; i < sineTable.length; i++) {
			float expected = sineTable[i];
			float actual = lookup(i);
			if (expected != actual) {
				throw new IllegalStateException("sine LUT error at index " + i + " (expected " + expected + ", got " + actual + ")");
			}
		}
	}

	public static float sin(float value) {
		return lookup((int)(value * 10430.378F) & 0xFFFF);
	}

	public static float cos(float value) {
		return lookup((int)(value * 10430.378F + 16384.0F) & 0xFFFF);
	}

	private static float lookup(int index) {
		if (index == 32768) {
			return midpoint;
		}

		// sin(-x) = -sin(x), so only the first half of the circle needs entries
		int negative = (index & 0x8000) << 16;
		// all bits set once past pi/2, none before
		int mask = (index << 17) >> 31;
		// sin(x) = sin(pi/2 - x) folds the second quarter onto the first
		int position = (0x8001 & mask) + (index ^ mask);

		return Float.intBitsToFloat(TABLE[position & 0x7FFF] ^ negative);
	}
}
