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

public class Correspondent{
	private final int port = 7000 //Listen port
	public static void main(String[] args){

		// ===== Variables =====
		String homeAgentIP = args[0]; //Command line arg: home agent IP


	} //main
} //class