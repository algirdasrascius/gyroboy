package com.mydigiverse.scanner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LineScannerTest {

	@Test
	public void testSimpe() {
		LineScanner scanner = new LineScanner();
		assertEquals(-1, scanner.black(0));
		scanner.white(3);
		assertEquals(-1, scanner.black(4));
		scanner.white(5);
		assertEquals(-1, scanner.black(7));
		scanner.white(8);
		assertEquals(-1, scanner.black(9));
		scanner.white(11);
		assertEquals(-1, scanner.black(13));
		scanner.white(14);
		assertEquals(-1, scanner.black(15));
		scanner.white(17);
		assertEquals(0b0010011001, scanner.black(18));
	}

	@Test
	public void testMultipleWidth() {
		LineScanner scanner = new LineScanner();
		assertEquals(-1, scanner.black(-20));
		scanner.white(-10);

		assertEquals(-1, scanner.black(0));
		scanner.white(30);
		assertEquals(-1, scanner.black(40));
		scanner.white(50);
		assertEquals(-1, scanner.black(70));
		scanner.white(80);
		assertEquals(-1, scanner.black(90));
		scanner.white(110);
		assertEquals(-1, scanner.black(130));
		scanner.white(140);
		assertEquals(-1, scanner.black(150));
		scanner.white(170);
		assertEquals(0b0010011001, scanner.black(180));

		scanner.white(190);
		assertEquals(-1, scanner.black(200));
	}

}
