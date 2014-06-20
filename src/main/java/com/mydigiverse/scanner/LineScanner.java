package com.mydigiverse.scanner;

/**
 * Class for decoding barcodes from black and white bar sequence.
 * 
 * The valid barcode consists of following elements:
 *   1. Leading black bar with width of at least 3 units (may be more).
 *   2. Ten alternating white and black bars, of which 3 white bars and 3 black bars are of single width and
 *      two white bars and 2 black bars are of double width.
 *   3. One traling white bar of single width.
 *   4. Trailing black bar.
 *   
 * Barcode value is encoded by widths of ten alternating white and black bars (as defined in item 2) each bar
 * representing single binary digit. Single width bar represents zero, double width bar represents one. Most 
 * significant bit is encoded first. Note that not all binary bit sequences are encodable as it is required
 * that there are exactly 2 wide black and 2 wide white bars. 
 * 
 * For example following bar sequence represents value 153 (binary 0010011001):
 * 
 *   █████████████___███______███___██████______███___██████___███████████████
 *         ^       ^  ^   ^    ^  ^   ^     ^    ^  ^   ^    ^          ^
 *         |       0  0   1    0  0   1     1    0  0   1    |          |
 *      Leading                                           Trailing   Trailing     
 *     black bar                                         white bar  black bar
 *        
 * 
 */
public class LineScanner {
	public static final int INVALID_CODE = -1;
	
	private static final int CODE_BAR_COUNT = 10;
	private static final int SPAN_COUNT = 1 + CODE_BAR_COUNT + 1; // Trailing black bar not included
	private static final int WIDE_COUNT = 2;	
	private static final int TICK_COUNT = CODE_BAR_COUNT + 2 * WIDE_COUNT + 1; // Code bars + white trailing bar 
	private static final int TICK_COUNT_X10 = TICK_COUNT * 10; 
	private static final int MIN_LEADING_FACTOR_X10 = 25;
	private static final int MIN_NARROW_FACTOR_X10 = 7;
	private static final int MAX_NARROW_FACTOR_X10 = 13;
	private static final int MIN_WIDE_FACTOR_X10 = 15;
	private static final int MAX_WIDE_FACTOR_X10 = 25;
	
	private final int[] points = new int[SPAN_COUNT + 1];
	private int index = 0;
		
	public LineScanner() {
		super();
	}

	public void white(int x) {
		if (whiteExpected()) {
			points[index++] = x;
		}
	}

	public int black(int x) {
		int result = INVALID_CODE;
		if (blackExpected()) {
			points[index++] = x;
			if (index > SPAN_COUNT) {
				result = match();
				skipOldest();
			}
		}
		return result;
	}

	public void reset() {
		index = 0;
	}
	
	private boolean blackExpected() {
		return index % 2 == 0;
	}

	private boolean whiteExpected() {
		return !blackExpected();
	}

	private int match() {
		int totalUnits = points[SPAN_COUNT] - points[1];
		float tickSize = totalUnits / TICK_COUNT;
		
		int minLeadingUnits = totalUnits * MIN_LEADING_FACTOR_X10 / TICK_COUNT_X10;
		
		// Check that leading black bar is wide enough 
		int leadingSize = points[1] - points[0];
		if (leadingSize < minLeadingUnits) {
			// Leading black bar its too narrow
			return INVALID_CODE;
		}

		int minNarrowUnits = totalUnits * MIN_NARROW_FACTOR_X10 / TICK_COUNT_X10;
		int maxNarrowUnits = totalUnits * MAX_NARROW_FACTOR_X10 / TICK_COUNT_X10;

		// Check that training white bar is narrow 
		int tralingSize = points[SPAN_COUNT] - points[SPAN_COUNT - 1];
		if (tralingSize < minNarrowUnits || tralingSize > maxNarrowUnits) {
			// Trailing white bar its not single width
			return INVALID_CODE;
		}

		int minWideUnits = totalUnits * MIN_WIDE_FACTOR_X10 / TICK_COUNT_X10;
		int maxWideUnits = totalUnits * MAX_WIDE_FACTOR_X10 / TICK_COUNT_X10;

		// Check Calculate code
		int code = 0;
		int wideBarCount = 0; 
		int wideSpaceCount = 0;
		for (int i = 2; i < SPAN_COUNT; i++) {
			int barSize = points[i] - points[i-1];
			if (minNarrowUnits <= barSize && barSize <= maxNarrowUnits) {
				// Narrow span
				code = code << 1;
			} else if (minWideUnits <= barSize && barSize <= maxWideUnits) {
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
