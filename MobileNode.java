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
// Program's Operational Status: Awaiting Testing
///////////////////////////////////////////////////////////////////

import java.net.*;

public class MobileNode {
	public static void main(String[] args) {
		String homeAddr, foreignAddr;
		DatagramSocket socket = new DatagramSocket(Frame.MOBILE_PORT);
		Frame frame = null;
		Scanner input = new Scanner(System.in);
		boolean terminate = false;
		if (args.length < 2) {
			System.out.println("Usage: java -jar MobileNode <home_agent_IP_address> <foreign_agent_IP_address>");
			System.exit(0);
		}
		homeAddr=args[0];
		foreignAddr=args[1];
		socket.setSoTimeout(1000);
		
		while (!terminate) {
			int option;
			System.out.println("----------------------------------------");
			System.out.println("Select one option from below:");
			System.out.println("   1 - Register with Home Agent");
			System.out.println("   2 - Deregister with Foreign Agent");
			System.out.println("   3 - Terminate program");
			option = input.nextInt();
			switch (option) {
				case 1: { // Register with Foreign Agent
					frame = new Frame(1,homeAddr,homeAddr,"");
					frame.send(socket, foreignAddr,Frame.FOREIGN_PORT);
					
					DatagramPacket packet = null;
					Frame recvFrame;
					String inputMsg = "";
					while (true) { // Listen for messages
						try {
							socket.receive(packet);
						} catch (SocketTimeoutException e) {
							continue;
						}
						break;
					}
					recvFrame = new Frame(packet.getData());
					if (recvFrame.getType()==8) {
						// Respond to message
						System.out.println(recvFrame.getMsg());
						inputMsg=input.nextLine();
						frame = new Frame(9,"","",inputMsg);
						frame.send(socket,recvFrame.getAddrA(),Frame.FOREIGN_PORT);
					}
					break;
				}
				case 2: { // Deregister with Foreign Agent
					frame = new Frame(2,homeAddr,homeAddr,"");
					frame.send(socket,foreignAddr,Frame.FOREIGN_PORT);
					break;
				}
				case 3: { // Terminate Program
					terminate=true;
				}	
			}				
		}
	}
}