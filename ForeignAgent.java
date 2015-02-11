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
// Program's Operational Status: Operational
///////////////////////////////////////////////////////////////////

import java.io.*;
import java.net.*;

public class ForeignAgent {
	private final int port = 8000;
	public static void main(String[] args) {
		DatagramSocket socket=null;;
		String homeAddr="",mobileAddr="",localAddr="";
		//Welcome message
		System.out.println("---------------------------------------");
		System.out.println("Foreign Agent is starting up...\n");
		try {
			socket = new DatagramSocket(FrameHandler.FOREIGN_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(0);
		} // try
		try {
			socket.setSoTimeout(1000);
		} catch(SocketException e) {
			e.printStackTrace();
			System.exit(0);
		} // try
		//Display current IP
		try {
			// localAddr=Inet4Address.getLocalHost().getHostAddress();
			//System.out.println("Current IP Address is: " + localAddr + "\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		} // try
		System.out.println("---------------------------------------");
		System.out.println("Listening...");
		while (true) {
			DatagramPacket packet = new DatagramPacket(new byte[64],64);
			Frame recvFrame, sendFrame;
			recvFrame = FrameHandler.recv(socket,packet);
			switch (FrameHandler.getType(recvFrame)) {
				case 0: { //Mobile Node is shutting down
					System.out.println("SHUTDOWN: mobile node is shutting down");
					break;
				}
				case 1: { //Register Mobile with Foreign
					localAddr=FrameHandler.getIpAddrA(recvFrame);
					homeAddr = FrameHandler.getIpAddrB(recvFrame);
					mobileAddr = packet.getAddress().toString().replace("/","");
					
					sendFrame = FrameHandler.create(3,localAddr,homeAddr,"");
					FrameHandler.send(socket,homeAddr,FrameHandler.HOME_PORT,sendFrame);
					System.out.println("REGISTER: " + mobileAddr);
					break;
				} // case
				case 2: { //Deregister Mobile with Foreign
					sendFrame = FrameHandler.create(4,localAddr,homeAddr,"");
					FrameHandler.send(socket,homeAddr,FrameHandler.HOME_PORT,sendFrame);
					System.out.println("DEREGISTER: " + mobileAddr);
					mobileAddr = "";
					break;
				} // case
				case 7: { //Send Message to Mobile
					sendFrame = FrameHandler.create(8,FrameHandler.getIpAddrA(recvFrame),"",FrameHandler.getMsg(recvFrame));
					FrameHandler.send(socket,mobileAddr,FrameHandler.MOBILE_PORT,sendFrame);
					System.out.println("MSG: Sent to mobile node at " + mobileAddr);
					break;
				} // case
			} // switch
		} // while
	} // main(String[])
} // class