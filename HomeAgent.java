///////////////////////////////////////////////////////////////////
// Student name: Sam Mills
// Course: COSC 4653 - Advanced Networks
// Assignment: #3 - Mobile IP Simulation
// File name: HomeAgent.java
// Program's Purpose: The home agent for the mobile-IP simulation.
//    Three functions:
//       - Receive message from Foreign Agent to register/deregister
//         a mobile node
//       - Receive a message from correspondent to send to a mobile
//         node
//       - Forward a message from a correspondent to a mobile node
//         through the Foreign Agent
//
// Program's Limitations:
//    -Only supports one mobile node (easy fix, already keeping
//        track of the number of nodes, just needs an implementation
//        of a search that searches the regTable for the desired
//        node)
//    -No interaction currently (no args or user input)
// Program details:
//    Frame types:
//     -Receive types{
//       - Type 0 (Correspondent -> Home Agent): Shut Down
//            |Correspondent informing that it is shutting down.|
//       - Type 3 (Foreign Agent -> Home Agent): Register
//            |Register mobile node with Home Agent             |
//       - Type 4 (Foreign Agent -> Home Agent): Deregister
//            |Deregister mobile node with Home Agent           |
//       - Type 5 (Correspondent -> Home Agent): Send Message (C)
//            |Sends a message to a mobile node                 |
//     -Send types{
//       - Type 6 (Home Agent > Correspondent): Inform Correspondent
//            |Tells correspondent that no mobile node with the |
//            |specified IP address is registered.              |
//       - Type 7 (Home Agent -> Foreign Agent): Send Message (F)
//            |Sends a message to a mobile node                 |
// Development Computer: Surface Pro 3 (Xeta)(Core i5)
// Operating System: Windows 8.1
// Integrated Development Environment (IDE): Sublime Text 3
// Compiler: Java 1.8.0_25
// Program's Operational Status: Incomplete
///////////////////////////////////////////////////////////////////

import java.io.*;
import java.lang.*;
import java.net.*;
import java.nio.*;
import java.util.*;


public class HomeAgent{
	private final int port = 7000
	public static void main(String[] args)
	{
		//Welcome message
		System.out.println("---------------------------------------");
		System.out.println("Home Agent is starting up...\n");

		//Variables
		String homeIP = Inet4Address.getLocalHost().getHostAddress();
		regTable = new String [1][2];//for registration of nodes:
									 //[care of address][node address]
		int nodeNum = 0; //Used for registration, keeps track of nodes
						 //-is not used much in this program, only one
						 // node will exist

		//Display current IP
		System.out.println("Current IP Address is: " + homeIP + "\n");
		System.out.println("---------------------------------------");

		//===========================================================//
		//==                   Listening Socket                    ==//
		//===========================================================//

		//try statement attempts to create a UDP socket on the port
		try{
			//Listening Socket
			DatagramSocket serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(1000); //set timeout to 1 second
		} catch (SocketException e){
			//If creation fails, display error info and quit
			e.printStackTrace();
			System.exit(0);
		} //try

		//-----------------------------//
		//--     Start Listening     --//
		//-----------------------------//
		System.out.println("\nListening...\n");
		while (true){
			DatagramPacket packet = null; //Active packet
			Frame recvFrame, sendFrame; //Active frames

			//Try to recieve a packet from the network
			try {
				socket.receive(packet);
			} catch (SocketTimeoutException e) {
				continue; //loop again if timeout
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}

			//Get a frame using the Frame and Framehandler classes
			recvFrame =new Frame(packet.getData());

			//----------------------------------------//
			//--     Handle every kind of frame     --//
			//----------------------------------------//
			// -That is, every kind we will recieve
			// -Below: case = frame type
			switch (FrameHandler.getType(recvFrame)) {
				case 0: { //Type 0 (Correspondent -> Home Agent)
					      //"Shut Down"
					System.out.println("The current Correspondent is 
						shutting down.")]
					break;
				} //case
				case 3: { //Type 3 (Foreign Agent -> Home Agent)
						  //"Register"
					//receive a registration from the foreign agent:
					//   -put frames address A into regTable "COA"
					//   -put frames address B into regTable "node IP"
					regTable[nodeNum][0] = FrameHandler.getIpAddrA(
						recvFrame);
					regTable[nodeNum][1] = FrameHandler.getIpAddrB(
						recvFrame);
					nodeNum +=1; //increases node count for next reg
					System.out.println("Mobile node registered with 
						Home Agent!\nCare Of Address: " + 
						regTable[0][0] + "\nIP Address: "+
						regTable[0][1] + "\n");
					break;
				} //case
				case 4: { //Type 4 (Foreign Agent -> Home Agent)
						  //"Deregister"
					//receive a deregistration from the foreign agent:
					//   -clears node from reg table
					nodeNum -=1; //decreases node count for next reg
					break;
					regTable[nodeNum][0] = null;
					regTable[nodeNum][1] = null;
					System.out.println("Mobile node deregistered.\n")
				} //case
				case 5: { //Type 5 (Correspondent -> Home Agent)
						  //"Send Message" Forward message from Cor...
					//first, see if Correspondent gives us a valid
					//address to send to.
					//  -If so, forward the message
					//  -If not, inform Correspondent of the mistake
					System.out.println("Message received from 
						Correspondent.")
					if(regTable[nodeNum-1][1].equals(
						FrameHandler.getIpAddrA(recvFrame))){
							sendFrame = FrameHandler.create(7,
								FrameHandler.getIpAddrA(recvFrame),"",
								FrameHandler.getMsg(recvFrame));
							FrameHandler.send(socket,regTable[0][1],
								FrameHandler.MOBILE_PORT,sendFrame);
							System.out.println("Sent the following
								message to the Mobile Node: "+ 
								FrameHandler.getMsg(recvFrame));
							break;
					} else{
						sendFrame = FrameHandler.create(6,
							"",homeIP,"");
						FrameHandler.send(socket,
							FrameHandler.getIpAddrA(recvFrame),
							FrameHandler.MOBILE_PORT,sendFrame);
						System.out.println("Address of mobile node 
							requested by Correspondent not found. 
							Sending this error to the Correspondent.");
						break;
					}
				} //case
			} //switch
		} //while
	} //main
} //class