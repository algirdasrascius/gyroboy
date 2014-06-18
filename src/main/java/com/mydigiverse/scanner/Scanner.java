package com.mydigiverse.scanner;

public class Scanner {

		private static final float[] LEVELS = { 0.1f, 0.12f, 0.14f, 0.16f, 0.18f,
			                                    0.2f, 0.22f, 0.24f, 0.26f, 0.28f,
			                                    0.3f, 0.32f, 0.34f, 0.36f, 0.38f,
			                                    0.4f, 0.42f, 0.44f, 0.46f, 0.48f };
		
		private final LineScanner[] scanners;
		private float previousX;
		private float previousValue;
		
		public Scanner() {
			scanners = new LineScanner[LEVELS.length];
			for (int i = 0; i < LEVELS.length; i++) {
				scanners[i] = new LineScanner();
			}
		}
		
		public int process(float x, float value) {
			return -1;
			
		}
}
