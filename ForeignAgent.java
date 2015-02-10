///////////////////////////////////////////////////////////////////
// Student name: Corey McCandless
// Course: COSC 4653 - Advanced Networks
// Assignment: #3 - Mobile IP Simulation
// File name: ForeignAgent.java
// Program's Purpose: Defines ForeignAgent class, contains main method for mccandlessForeignAgent.jar
//
// Program's Limitations:
// Development Computer: Lenovo Y400 (Intel Core i7-3630QM)
// Operating System: Windows 8.1
// Integrated Development Environment (IDE): Notepad++
// Compiler: Java 1.8.0_25
// Program's Operational Status: Awaiting Testing
///////////////////////////////////////////////////////////////////

import java.io.*;
import java.net.*;

public class ForeignAgent {
	private final int port = 8000;
	public static void main(String[] args) {
		DatagramSocket socket=null;;
		try {
			socket = new DatagramSocket(FrameHandler.FOREIGN_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(0);
		} // try
		String homeAddr="",mobileAddr="";
		try {
			socket.setSoTimeout(1000);
		} catch(SocketException e) {
			e.printStackTrace();
			System.exit(0);
		} // try
		System.out.println("Listening...");
		while (true) {
			String localAddr = "";
			DatagramPacket packet = null;
			Frame recvFrame, sendFrame;
			try {
				socket.receive(packet);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			} // try
			recvFrame =new Frame(packet.getData());
			switch (FrameHandler.getType(recvFrame)) {
				case 1: { //Register Mobile with Foreign
					try {
						localAddr = InetAddress.getLocalHost().toString();
					} catch (UnknownHostException e) {
						e.printStackTrace();
						System.exit(0);
					} // try
					homeAddr = FrameHandler.getIpAddrA(recvFrame);
					mobileAddr = packet.getAddress().toString();
					
					sendFrame = FrameHandler.create(3,localAddr,homeAddr,"");
					FrameHandler.send(socket,homeAddr,FrameHandler.HOME_PORT,sendFrame);
					break;
				} // case
				case 2: { //Deregister Mobile with Foreign
					try {
						localAddr = InetAddress.getLocalHost().toString();
					} catch (UnknownHostException e) {
						e.printStackTrace();
						System.exit(0);
					} // try
					sendFrame = FrameHandler.create(4,localAddr,homeAddr,"");
					FrameHandler.send(socket,homeAddr,FrameHandler.HOME_PORT,sendFrame);
					homeAddr="";
					mobileAddr = "";
					break;
				} // case
				case 7: { //Send Message to Mobile
					sendFrame = FrameHandler.create(8,FrameHandler.getIpAddrA(recvFrame),"",FrameHandler.getMsg(recvFrame));
					FrameHandler.send(socket,mobileAddr,FrameHandler.MOBILE_PORT,sendFrame);
					break;
				} // case
			} // switch
		} // while
	} // main(String[])
} // class