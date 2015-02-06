import java.io.*;
import java.lang.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class Frame {
	public static final int MAX_MSG_SIZE = 52;
	private byte[] data;
	
	// Primary constructor for Frame class. Avoid use of no-arg constructor
	public Frame(int type, String ipAddrA, String ipAddrB, String msg) {
		data=new byte[MAX_MSG_SIZE+12];
		int msgLen=(msg.length()>MAX_MSG_SIZE?MAX_MSG_SIZE:msg.length());
		int frameSize = 12+msgLen;
		for (int i=0;i<frameSize;i++) {
			if (i<4) {
				this.data[i]=ByteBuffer.allocate(4).putInt(type).array()[i];
			} else if (i<8) {
				this.data[i]=ipAddrA.getBytes()[i-4];
			} else if (i<12) {
				this.data[i]=ipAddrB.getBytes()[i-8];
			} else {
				this.data[i]=msg.getBytes()[i-12];
			}
			
		}
	}
	// Creates new Instance of Frame from a byte array. Truncates bytes that do not fit within the maximum frame size
	public Frame(byte[] data) {
		this.data = Arrays.copyOfRange(data,0,12+MAX_MSG_SIZE);
	}
	// Returns the Frame's data as a byte array
	public byte[] getData() {
		return data;
	}
	// Sets the Frame's type 
	public void setType(int type) {
		for (int i=0;i<4;i++)
			this.data[i]=ByteBuffer.allocate(4).putInt(type).array()[i];
	}
	// Returns Frame's type as an integer
	public int getType() {
		return (data[0]<<24) + (data[1]<<16) + (data[2]<<8) + data[3];
	}
	// Sets Frame's IP Address A
	public void setIpAddrA(String ipAddr) {
		for (int i=0;i<4;i++)
			this.data[i+4]=ipAddr.getBytes()[i];
	}
	// Returns IP Address A as a String
	public String getIpAddrA() {
		String result = "";
		try {
			result=new String(Arrays.copyOfRange(data,4,8),"UTF-8:");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	// Sets Frame's IP Address B
	public void setIpAddrB(String ipAddr) {
		for (int i=0;i<4;i++)
			this.data[i+8]=ipAddr.getBytes()[i];
	}
	// Returns IP Address B as a String
	public String getIpAddrB() {
		String result="";
		try {
			result=new String(Arrays.copyOfRange(data,8,12),"UTF-8:");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	// Sets String message contained in Frame
	public void setMsg(String msg) {
		for (int i=0;i<(msg.length()>MAX_MSG_SIZE?MAX_MSG_SIZE:msg.length());i++)
			this.data[i+12]=msg.getBytes()[i];
	}
	// Returns String message contained in Frame
	public String getMsg() {
		String result = "";
		try {
			result=new String(Arrays.copyOfRange(data,12,MAX_MSG_SIZE+12),"UTF-8:");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	// Non-Static method for sending a frame via UDP
	public void send(DatagramSocket socket, String toAddr, int port) {
		DatagramPacket packet = null;
		try {
			new DatagramPacket(data,MAX_MSG_SIZE+12,InetAddress.getByName(toAddr),port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Static method for sending data not yet organized into a frame via UDP
	public static void send(DatagramSocket socket, String toAddr, int port, int type, String ipAddrA, String ipAddrB, String msg) {
		Frame frame = new Frame(type, ipAddrA, ipAddrB, msg);
		DatagramPacket packet = null;
		try {
			new DatagramPacket(frame.data,MAX_MSG_SIZE+12,InetAddress.getByName(toAddr),port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Static method for receiving UDP datagram. Returns an instance of Frame
	public static Frame recv(DatagramSocket socket) {
		DatagramPacket packet=null;
		try {
			socket.receive(packet);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new Frame(packet.getData());
	}
}