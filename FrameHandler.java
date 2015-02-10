///////////////////////////////////////////////////////////////////
// Student name: Corey McCandless
// Course: COSC 4653 - Advanced Networks
// Assignment: #3 - Mobile IP Simulation
// File name: FrameHandler.java
// Program's Purpose: Defines FrameHandler class
//
// Program's Limitations:
// Development Computer: Lenovo Y400 (Intel Core i7-3630QM)
// Operating System: Windows 8.1
// Integrated Development Environment (IDE): Notepad++
// Compiler: Java 1.8.0_25
// Program's Operational Status: Awaiting Testing
///////////////////////////////////////////////////////////////////

import java.io.*;
import java.lang.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public abstract class FrameHandler {
	public static final int MOBILE_PORT = 9000;
	public static final int FOREIGN_PORT = 8000;
	public static final int HOME_PORT = 7000;
	public static final int CORRESPONDENT_PORT = 6000;
	
	public static final int MAX_MSG_SIZE = 52;
	
	// Used for creating a new instance of Frame. 
	public static Frame create(int type, String ipAddrA, String ipAddrB, String msg) {
		byte[] data=new byte[MAX_MSG_SIZE+12];
		int msgLen=(msg.length()>MAX_MSG_SIZE?MAX_MSG_SIZE:msg.length());
		int frameSize = 12+msgLen;
		for (int i=0;i<frameSize;i++) {
			if (i<4) {
				data[i]=ByteBuffer.allocate(4).putInt(type).array()[i];
			} else if (i<8) {
				data[i]=ipAddrA.getBytes()[i-4];
			} else if (i<12) {
				data[i]=ipAddrB.getBytes()[i-8];
			} else {
				data[i]=msg.getBytes()[i-12];
			}
		}
		return new Frame(data);
	} // create(int,String,String,String)
	// Returns the Frame's data as a byte array
	public static byte[] getData(Frame frame) {
		return frame.data;
	} // getData(Frame)
	// Sets the Frame's type 
	public static void setType(Frame frame,int type) {
		for (int i=0;i<4;i++)
			frame.data[i]=ByteBuffer.allocate(4).putInt(type).array()[i];
	} // setType(Frame,int)
	// Returns Frame's type as an integer
	public static int getType(Frame frame) {
		return (frame.data[0]<<24) + (frame.data[1]<<16) + (frame.data[2]<<8) + frame.data[3];
	} // getType(Frame)
	// Sets Frame's IP Address A
	public static void setIpAddrA(Frame frame,String ipAddr) {
		for (int i=0;i<4;i++)
			frame.data[i+4]=ipAddr.getBytes()[i];
	} // setIpAddrA(Frame,String)
	// Returns IP Address A as a String
	public static String getIpAddrA(Frame frame) {
		String result = "";
		try {
			result=new String(Arrays.copyOfRange(frame.data,4,8),"UTF-8:");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	} // getIpAddrA(Frame)
	// Sets Frame's IP Address B
	public static void setIpAddrB(Frame frame, String ipAddr) {
		for (int i=0;i<4;i++)
			frame.data[i+8]=ipAddr.getBytes()[i];
	} // setIpAddrB(Frame,String)
	// Returns IP Address B as a String
	public static String getIpAddrB(Frame frame) {
		String result="";
		try {
			result=new String(Arrays.copyOfRange(frame.data,8,12),"UTF-8:");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} // try
		return result;
	} // getIpAddrB(Frame)
	// Sets String message contained in Frame
	public static void setMsg(Frame frame, String msg) {
		for (int i=0;i<(msg.length()>MAX_MSG_SIZE?MAX_MSG_SIZE:msg.length());i++)
			frame.data[i+12]=msg.getBytes()[i];
	} // setMsg(Frame,String)
	// Returns String message contained in Frame
	public static String getMsg(Frame frame) {
		String result = "";
		try {
			result=new String(Arrays.copyOfRange(frame.data,12,MAX_MSG_SIZE+12),"UTF-8:");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} // try
		return result;
	} // getMsg(Frame)
	// Method for sending a frame via UDP
	public static void send(DatagramSocket socket, String toAddr, int port, Frame frame) {
		if (toAddr=="") {
			return;
		} // if
		DatagramPacket packet = null;
		try {
			new DatagramPacket(frame.data,MAX_MSG_SIZE+12,InetAddress.getByName(toAddr),port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} // try
		try {
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		} // try
	} // send(DatagramSocket,String,int,Frame)
	// Static method for sending data not yet organized into a frame via UDP
	public static void send(DatagramSocket socket, String toAddr, int port, int type, String ipAddrA, String ipAddrB, String msg) {
		if (toAddr=="") {
			return;
		} // if
		Frame frame = create(type, ipAddrA, ipAddrB, msg);
		DatagramPacket packet = null;
		try {
			new DatagramPacket(frame.data,MAX_MSG_SIZE+12,InetAddress.getByName(toAddr),port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} // try
		try {
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		} // try
	} // send(DatagramSocket,String,int,int,String,String,String)
	// Static method for receiving UDP datagram. Returns an instance of Frame
	public static Frame recv(DatagramSocket socket) {
		DatagramPacket packet=null;
		try {
			socket.receive(packet);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} // try
		return new Frame(packet.getData());
	} // recv(DatagramSocket)
} // class