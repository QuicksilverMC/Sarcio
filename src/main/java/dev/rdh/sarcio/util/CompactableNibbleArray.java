package dev.rdh.sarcio.util;

public interface CompactableNibbleArray {
	byte[] sarcio$writableData();

	void sarcio$compact();
}
