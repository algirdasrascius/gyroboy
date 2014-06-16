package com.digiverse.scanner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class ScannerTest {

	@Test
	public void test() {
		ScannerListener listener = mock(ScannerListener.class);
		
		Scanner scanner = new Scanner(listener);
		scanner.white(0);
		scanner.black(1);
		scanner.white(2);
		scanner.black(4);
		scanner.white(5);
		scanner.black(6);
		scanner.white(8);
		scanner.black(10);
		scanner.white(11);
		scanner.black(12);
		scanner.white(14);
		
		verify(listener).scanned(153);
	}

}
