package com.mydigiverse.scanner;

/**
 * Class for decoding barcodes from grayscale input.
 * 
 * This class maintains list of LineScanner instances that are set up to distinguish black and white input at different
 * grayscale value threshold. Barcode is recognized if any LineScanner recognizes it.
 * 
 */
public class Scanner {

	private static final float[] LEVELS = { 0.1f, 0.12f, 0.14f, 0.16f, 0.18f,
		                                    0.2f, 0.22f, 0.24f, 0.26f, 0.28f,
		                                    0.3f, 0.32f, 0.34f, 0.36f, 0.38f,
		                                    0.4f, 0.42f, 0.44f, 0.46f, 0.48f };
	private static final int LEVEL_COUNT = LEVELS.length;
	
	private final LineScanner[] lineScanners;
	private int minBlackLevel;
	
	public Scanner() {
		lineScanners = new LineScanner[LEVEL_COUNT];
		for (int i = 0; i < LEVEL_COUNT; i++) {
			lineScanners[i] = new LineScanner();
		}
		minBlackLevel = LEVEL_COUNT;
	}
	
	public int process(float x, float value) {
		int result = LineScanner.INVALID_CODE;
		while (minBlackLevel < LEVEL_COUNT && value > LEVELS[minBlackLevel]) {
			lineScanners[minBlackLevel].white(x);
			minBlackLevel++;
		}
		while (minBlackLevel > 0 && value <= LEVELS[minBlackLevel - 1]) {
			minBlackLevel--;
			int lineResult = lineScanners[minBlackLevel].black(x);
			if (lineResult != LineScanner.INVALID_CODE) {
				result = lineResult;
			}
		}
		return result;
	}
	
	public void reset() {
		for (int i = 0; i < LEVEL_COUNT; i++) {
			lineScanners[i].reset();
		}		
		minBlackLevel = LEVEL_COUNT;
	}
	
}
