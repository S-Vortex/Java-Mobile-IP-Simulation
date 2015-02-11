///////////////////////////////////////////////////////////////////
// Student name: Corey McCandless
// Course: COSC 4653 - Advanced Networks
// Assignment: #3 - Mobile IP Simulation
// File name: MobileNode.java
// Program's Purpose: Defines MobileNode class, contains main method for mccandlessMobileNode.jar
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
import java.util.*;

public class MobileNode {
	public static void main(String[] args) {
		String homeAddr, foreignAddr;
		Frame frame = null;
		Scanner input = new Scanner(System.in);
		boolean terminate = false,registered=false;
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(FrameHandler.MOBILE_PORT);
			socket.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(0);
		} // try
		if (args.length < 2) {
			System.out.println("Usage: java -jar MobileNode <home_agent_IP_address> <foreign_agent_IP_address>");
			System.exit(0);
		} // if
		homeAddr=args[0];
		foreignAddr=args[1];
		
		while (!terminate) {
			int option;
			System.out.println("---------------------------------------");
			try {
				System.out.println("Current IP is: " + Inet4Address.getLocalHost().getHostAddress());
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			} // try
			System.out.println("STATUS: " + (registered?"REGISTERED":"NOT REGISTERED"));
			System.out.println("---------------------------------------");
			System.out.println("Select one option from below:");
			System.out.println("   1 - Register with Home Agent");
			System.out.println("   2 - Deregister with Foreign Agent");
			System.out.println("   3 - Terminate program");
			option = input.nextInt();
			input.nextLine();
			switch (option) {
				case 1: { // Register with Foreign Agent
					String inputMsg;
					Frame recvFrame;
					frame = FrameHandler.create(1,homeAddr,homeAddr,"");
					FrameHandler.send(socket, foreignAddr,FrameHandler.FOREIGN_PORT,frame);
					inputMsg = "";
					recvFrame = FrameHandler.recv(socket,null,".");
					registered=true;
					if (FrameHandler.getType(recvFrame)==8) {
						// Respond to message
						System.out.println("\n"+FrameHandler.getMsg(recvFrame)+"\n");
						System.out.print("Type your response: ");
						inputMsg=input.nextLine();
						frame = FrameHandler.create(9,"","",inputMsg);
						FrameHandler.send(socket,FrameHandler.getIpAddrA(recvFrame),FrameHandler.CORRESPONDENT_PORT,frame);
						System.out.println("");
					} // if
					break;
				} // case
				case 2: { // Deregister with Foreign Agent
					frame = FrameHandler.create(2,homeAddr,homeAddr,"");
					FrameHandler.send(socket,foreignAddr,FrameHandler.FOREIGN_PORT,frame);
					registered=false;
					break;
				} // case
				case 3: { // Terminate Program
					terminate=true;
					FrameHandler.send(socket,foreignAddr,FrameHandler.FOREIGN_PORT,0,"","","");
				} // case
			} // switch
		} // while
	} // main(String[])
} // class