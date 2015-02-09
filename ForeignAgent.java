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

import java.net.*;

public class ForeignAgent {
	private final int port = 8000;
	public static void main(String[] args) {
		DatagramSocket socket = new DatagramSocket(port);
		String homeAddr="",mobileAddr="";
		socket.setSoTimeout(1000);
		System.out.println("Listening...");
		while (true) {
			DatagramPacket packet = null;
			Frame recvFrame, sendFrame;
			try {
				socket.receive(packet);
			} catch (SocketTimeoutException e) {
				continue;
			}
			recvFrame = new Frame(packet.getData());
			switch (recvFrame.getType()) {
				case 1: { //Register Mobile with Foreign
					homeAddr = recvFrame.getAddrA();
					mobileAddr = packet.getAddress().toString();
					sendFrame = new Frame(3,InetAddress.getLocalHost(),homeAddr)
					sendFrame.send(socket,homeAddr,Frame.HOME_PORT);
					break;
				}
				case 2: { //Deregister Mobile with Foreign
					sendFrame = new Frame(4,InetAddress.getLocalHost(),homeAddr);
					sendFrame.send(socket,homeAddr,Frame.HOME_PORT);
					homeAddr="";
					mobileAddr = "";
					break;
				}
				case 7: { //Send Message to Mobile
					sendFrame = new Frame(8,recvFrame.getAddrA(),"",recvFrame.getMsg());
					sendFrame.send(socket,mobileAddr,Frame.MOBILE_PORT);
					break;
				}
			}
		}
	}
}