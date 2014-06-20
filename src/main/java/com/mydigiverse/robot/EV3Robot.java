package com.mydigiverse.robot;

import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

import com.mydigiverse.scanner.Scanner;

public class EV3Robot {
	
    public static void main(String[] args) {
    	try (RegulatedMotor motor1 = new EV3LargeRegulatedMotor(MotorPort.B);
    		 RegulatedMotor motor2 = new EV3LargeRegulatedMotor(MotorPort.C)) {
    		
    		int speed = 150;
    		motor1.setSpeed(speed);
    		motor2.setSpeed(speed);
    		motor1.resetTachoCount();
    		motor2.resetTachoCount();
    		motor1.forward();
    		motor2.forward();
	  	  
    		EV3 ev3 = LocalEV3.get();

    		try (EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4)) {
    			SensorMode redMode = colorSensor.getRedMode();
	    		float[] value = new float[1];
	
	    		int i = 0;
	    		Scanner scanner = new Scanner();
	    		do {
	    			//int tachoCount = motor1.getTachoCount();
	    			redMode.fetchSample(value, 0);
	    			int result = scanner.process(i++, value[0]);
	    			if (result >= 0) {
	    				ev3.getTextLCD().drawString("Found: " + result + " " + i, 0, 4);
	    				ev3.getAudio().playTone(1000, 200);
	    				scanner.reset();
	    			}
	    			Delay.msDelay(1);
	    		} while (!Button.ESCAPE.isDown());
			  
	    	}
	    	motor1.stop();
	    	motor2.stop();
    	}
    }
}
