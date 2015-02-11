///////////////////////////////////////////////////////////////////////
// Student name: Sam Mills, Corey McCandless
// Course: COSC 4653 - Advanced Networks
// Assignment: #3 - Mobile IP Simulation
// File name: Correspondent.java
// Program's Purpose: The Corrrespondent for the mobile-IP simulation. 
//
// Program's Limitations:
// Program details:
//     -Send types{
//       - Type 0 (Correspondent -> Home Agent): Shut Down
//            |Correspondent informing that it is shutting down.|
//       - Type 5 (Correspondent -> Home Agent): Send Message
//            |Sends a message to the mobile node.              |
//     -Receive Types{
//       - Type 6 (Home Agent > Correspondent): Inform Correspondent
//            |Tells correspondent that no mobile node with the |
//            |specified IP address is registered.              |
//       - Type 9 (Mobile Node -> Correspondent): Send Message
//            |sends message to the correspondent               |
// Development Computer: Surface Pro 3
// Operating System: Windows 8.1
// Integrated Development Environment (IDE): Sublime Text 3
// Compiler: Java 1.8.0_25
// Program's Operational Status: Incomplete
///////////////////////////////////////////////////////////////////////
import java.io.*;
import java.lang.*;
import java.net.*;
import java.nio.*;
import java.util.*;


public class Correspondent{
	private static final int port = FrameHandler.CORRESPONDENT_PORT; //Listen port
	public static void main(String[] args){
		// ===== Variables =====
		String homeAgentIP = "";
		boolean terminated = false;
		DatagramSocket socket = null;
		Scanner input = new Scanner(System.in);
		String currIP = "";
		try {
			currIP = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		} // try
		
		//Startup
		if (args.length < 1) {
			System.out.println("Usage: java Correspondent.jar <home_agent_IP_address>");
			System.exit(0);
		} // if
		
		System.out.println("------------------------------------");
		System.out.println("Current IP is: " + currIP);

		homeAgentIP = args[0]; //Command line arg: home agent IP
		
		//===========================================================//
		//==                        Socket                         ==//
		//===========================================================//

		//try statement attempts to create a UDP socket on the port
		try{
			//Create the socket
			socket = new DatagramSocket(port);
			socket.setSoTimeout(1000); //set timeout to 1 second
		} catch (SocketException e){
			//If creation fails, display error info and quit
			e.printStackTrace();
			System.exit(0);
		} //try


		//------------------//
		//--     Start    --//
		//------------------//
		while (!terminated) {
			int option;
			cMenu();
			option = input.nextInt();
			input.nextLine();
			switch (option) {
				case 0: // Close
				{
					terminated=true;
					FrameHandler.send(socket,homeAgentIP,FrameHandler.HOME_PORT,0,"","","");
					break;
				} // case 0
				case 1: // Send message
				{
					Frame recvFrame = null;
					Frame sendFrame = null;
					String msg;
					System.out.print("Type your message: ");
					msg = input.nextLine();
					sendFrame = FrameHandler.create(5,currIP,homeAgentIP,msg);
					FrameHandler.send(socket,homeAgentIP,FrameHandler.HOME_PORT,sendFrame);
					System.out.print("Message sent to " + homeAgentIP + "; Waiting for response");
					recvFrame = FrameHandler.recv(socket,null,".");
					switch (FrameHandler.getType(recvFrame)) {
						case 6: //Mobile node not registered
						{
							System.out.println("\nMessage not sent: Mobile node not registered");
							break;
						} // case 6
						default: 
						{
							System.out.println("\n"+FrameHandler.getMsg(recvFrame)+"\n");
							break;
						} // default
					} // switch
					break;
				} // case 1
			} // switch
		} // while
	} //main

	// Prints menu options
	public static void cMenu(){
			System.out.println("------------------------------------");
			System.out.println("Select an option from below:");
			System.out.println("   0 - Close this program");
			System.out.println("   1 - Send a message");
	} // cMenu()
} //class