///////////////////////////////////////////////////////////////////
// Student name: Corey McCandless
// Course: COSC 4653 - Advanced Networks
// Assignment: #3 - Mobile IP Simulation
// File name: Frame.java
// Program's Purpose: Defines Frame class
//
// Program's Limitations:
// Development Computer: Lenovo Y400 (Intel Core i7-3630QM)
// Operating System: Windows 8.1
// Integrated Development Environment (IDE): Notepad++
// Compiler: Java 1.8.0_25
// Program's Operational Status: Awaiting Testing
///////////////////////////////////////////////////////////////////

import java.util.*;

public class Frame {
	public  byte[] data = new byte[64];
	// Creates new Instance of Frame from a byte array. Truncates bytes that do not fit within the maximum frame size
	public Frame(byte[] data) {
		this.data = Arrays.copyOfRange(data,0,this.data.length);
	} // Frame(byte[])
} // class