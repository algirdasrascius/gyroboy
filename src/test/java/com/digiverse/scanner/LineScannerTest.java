package com.digiverse.scanner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LineScannerTest {

	@Test
	public void testSimpe() {
		LineScanner scanner = new LineScanner();
		assertEquals(-1, scanner.white(0));
		scanner.black(1);
		assertEquals(-1, scanner.white(2));
		scanner.black(4);
		assertEquals(-1, scanner.white(5));
		scanner.black(6);
		assertEquals(-1, scanner.white(8));
		scanner.black(10);
		assertEquals(-1, scanner.white(11));
		scanner.black(12);
		assertEquals(0b0010011001, scanner.white(14));
	}

	@Test
	public void testMultipleWidth() {
		LineScanner scanner = new LineScanner();
		assertEquals(-1, scanner.white(-100));
		scanner.black(-30);
		assertEquals(-1, scanner.white(0));
		scanner.black(10);
		assertEquals(-1, scanner.white(20));
		scanner.black(40);
		assertEquals(-1, scanner.white(50));
		scanner.black(60);
		assertEquals(-1, scanner.white(80));
		scanner.black(100);
		assertEquals(-1, scanner.white(110));
		scanner.black(120);
		assertEquals(0b0010011001, scanner.white(140));
	}

}
