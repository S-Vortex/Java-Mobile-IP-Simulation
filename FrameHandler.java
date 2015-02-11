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
	private static final boolean DEBUG = false;
	
	public static final int MOBILE_PORT = 9000;
	public static final int FOREIGN_PORT = 8000;
	public static final int HOME_PORT = 7000;
	public static final int CORRESPONDENT_PORT = 6000;
	
	public static final int MAX_MSG_SIZE = 52;
	
	// Used for creating a new instance of Frame. 
	public static Frame create(int type, String ipAddrA, String ipAddrB, String msg) {
		byte[] data=new byte[MAX_MSG_SIZE+12];
		int msgLen=(msg.length()>MAX_MSG_SIZE?MAX_MSG_SIZE:msg.length());
		for (int i=0;i<(msgLen>4?msgLen:4);i++) {
			if (i<4) {
				data[i]=ByteBuffer.allocate(4).putInt(type).array()[i];
				try {
					if (ipAddrA!="") data[i+4]=InetAddress.getByName(ipAddrA).getAddress()[i];
					if (ipAddrB!="") data[i+8]=InetAddress.getByName(ipAddrB).getAddress()[i];
				} catch(UnknownHostException e) {
					e.printStackTrace();
					return null;
				} // try
			} // if
			if (msg!="") data[i+12]=msg.getBytes()[i];
		} // for
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
		try {
			for (int i=0;i<4;i++)
				frame.data[i+4]=InetAddress.getByName(ipAddr).getAddress()[i];
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} // try
	} // setIpAddrA(Frame,String)
	
	// Returns IP Address A as a String
	public static String getIpAddrA(Frame frame) {
		String result = "";
		try {
			result=InetAddress.getByAddress(Arrays.copyOfRange(frame.data,4,8)).getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} // try
		return result;
	} // getIpAddrA(Frame)
	
	// Sets Frame's IP Address B
	public static void setIpAddrB(Frame frame, String ipAddr) {
		try {
			for (int i=0;i<4;i++)
				frame.data[i+8]=InetAddress.getByName(ipAddr).getAddress()[i];
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} //try
	} // setIpAddrB(Frame,String)
	
	// Returns IP Address B as a String
	public static String getIpAddrB(Frame frame) {
		String result="";
		try {
			result=InetAddress.getByAddress(Arrays.copyOfRange(frame.data,8,12)).getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
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
			result=new String(Arrays.copyOfRange(frame.data,12,MAX_MSG_SIZE+12),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} // try
		return result;
	} // getMsg(Frame)
	
	// Method for sending a frame via UDP
	public static void send(DatagramSocket socket, String toAddr, int port, Frame frame) {
		DatagramPacket packet=null;
		if (toAddr=="") {
			return;
		} // if
		try {
			packet = new DatagramPacket(frame.data,MAX_MSG_SIZE+12,InetAddress.getByName(toAddr),port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println(toAddr + " is a bad address");
			return;
		} // try
		try {
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		} // try
		debugPrint(String.format("MSG_SENT: %s:%d\n   %d %s %s",packet.getAddress(),packet.getPort(),getType(frame),getIpAddrA(frame),getIpAddrB(frame)));
	} // send(DatagramSocket,String,int,Frame)
	
	// Static method for sending data not yet organized into a frame via UDP
	public static void send(DatagramSocket socket, String toAddr, int port, int type, String ipAddrA, String ipAddrB, String msg) {
		send(socket,toAddr,port,create(type,ipAddrA,ipAddrB,msg));
	} // send(DatagramSocket,String,int,int,String,String,String)
	
	// Static method for receiving UDP datagram. Returns an instance of Frame
	public static Frame recv(DatagramSocket socket,DatagramPacket packet) {
		return recv(socket,packet,"");
	} // recv(DatagramSocket,DatagramPacket)
	// Static method for receiving UDP datagram with optional "waiting" characters
	
	public static Frame recv(DatagramSocket socket,DatagramPacket packet, String waitChars) {
		boolean timeout=false;
		Frame result = null;
		if (packet==null) packet = new DatagramPacket(new byte[64],64);
		if (socket==null) return null;
		try {
			timeout=socket.getSoTimeout()>0;
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(0);
		} // try
		if (timeout) {
			while (true) {
				try {
					socket.receive(packet);
				} catch (SocketTimeoutException e) {
					System.out.print(waitChars);
					continue;
				}catch (Exception e) {
					e.printStackTrace();
					return null;
				} // try
				break;
			} // while
			System.out.println("");
		} else {
			try {
				socket.receive(packet);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} // try
		} // if
		result = new Frame(packet.getData());
		debugPrint(String.format("MSG_RECVD: %s:%d\n   %d %s %s",packet.getAddress(),packet.getPort(),getType(result),getIpAddrA(result),getIpAddrB(result)));
		return result;
	} // recv(DatagramSocket)
	
	private static void debugPrint(String msg) {
		if (DEBUG) System.out.println(msg);
	} // debugPrint(String)
} // class