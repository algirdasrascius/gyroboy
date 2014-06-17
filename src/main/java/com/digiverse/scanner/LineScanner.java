package com.digiverse.scanner;


public class LineScanner {
	public static final int INVALID_CODE = -1;
	private static final int BAR_COUNT = 5;
	private static final int SPAN_COUNT = BAR_COUNT * 2;
	private static final int WIDE_COUNT = 2;	
	private static final int TICK_COUNT = (BAR_COUNT + WIDE_COUNT) * 2;
	private static final float MIN_NARROW_FACTOR = 0.7f;
	private static final float MAX_NARROW_FACTOR = 1.3f;
	private static final float MIN_WIDE_FACTOR = 1.5f;
	private static final float MAX_WIDE_FACTOR = 2.5f;
	
	private final float[] points = new float[SPAN_COUNT + 1];
	private int index = 0;
		
	public LineScanner() {
		super();
	}

	public int white(float x) {
		int result = INVALID_CODE;
		if (whiteExpected()) {
			points[index++] = x;
			if (index > SPAN_COUNT) {
				result = match();
				skipOldest();
			}
		}
		return result;
	}

	public void black(float x) {
		if (!whiteExpected()) {
			points[index++] = x;
		}
	}

	public void reset() {
		index = 0;
	}
	
	private boolean whiteExpected() {
		return index % 2 == 0;
	}

	private int match() {
		float tickSize = (points[SPAN_COUNT] - points[0]) / TICK_COUNT;		
		int code = 0;
		int wideBarCount = 0; 
		int wideSpaceCount = 0;
		for (int i = 0; i < SPAN_COUNT; i++) {
			float spanSize = points[i + 1] - points[i];
			float spanFactor = spanSize / tickSize;
			if (MIN_NARROW_FACTOR <= spanFactor && spanFactor <= MAX_NARROW_FACTOR) {
				// Narrow span
				code = code << 1;
			} else if (MIN_WIDE_FACTOR <= spanFactor && spanFactor <= MAX_WIDE_FACTOR) {
				// Wide span
				code = (code << 1) | 1;
				if (i % 2 == 0) {
					wideSpaceCount++;
				} else {
					wideBarCount++;
				}
			} else {
				// Span is neither narrow nor wide - code is unreadeble
				return INVALID_CODE;
			}
		}
		return (wideBarCount == WIDE_COUNT && wideSpaceCount == WIDE_COUNT) ? code : INVALID_CODE;
	}

	private void skipOldest() {
		System.arraycopy(points, 2, points, 0, SPAN_COUNT - 1);
		index -= 2;
	}
	
}
