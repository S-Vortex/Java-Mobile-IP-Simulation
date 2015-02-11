///////////////////////////////////////////////////////////////////////
// Student name: Sam Mills
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
	private final int port = 7000 //Listen port
	public static void main(String[] args){
		//Startup
		if (args.length < 1) {
			System.out.println("Usage: java Correspondent.jar 
				<home_agent_IP_address>");
			System.exit(0);
		} // if

		System.out.println("---------------------------------------");
		// ===== Variables =====
		String homeAgentIP = args[0]; //Command line arg: home agent IP
		String currIP = Inet4Address.getLocalHost().getHostAddress();
			System.out.println("Current IP is: " + currIP);
		Frame frame = null;
		boolean terminated = false;
		Scanner input = new Scanner(System.in);

		//===========================================================//
		//==                        Socket                         ==//
		//===========================================================//

		//try statement attempts to create a UDP socket on the port
		try{
			//Create the socket
			DatagramSocket socket = new DatagramSocket(port);
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
			switch (option) {

			}





	} //main

	public static void cMenu(){
			System.out.println("------------------------------------");
			System.out.println("Select an option from below:");
			System.out.println("   0 - Close this program");
			System.out.println("   1 - Send a message");

			return 0;
	}
} //class