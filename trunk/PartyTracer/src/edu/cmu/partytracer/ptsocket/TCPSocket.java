package edu.cmu.partytracer.ptsocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * TCP version of PTSocket implementation, untested
 * @author km
 *
 */
public class TCPSocket implements PTSocket {
	InetAddress destIp;
	int destPort;
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	public TCPSocket(String destAddr, int destPort) throws IOException {
		destIp = InetAddress.getByName(destAddr);
		socket = new Socket(destIp, destPort);
		this.destPort = destPort;
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	
	public void sendObject(Object obj) throws IOException {
		oos.writeObject(obj);
		oos.flush();
	}
	
	public Object receiveObject() throws IOException, ClassNotFoundException {
		return ois.readObject();
	}
	
	public void close() throws IOException {
		ois.close();
		oos.close();
		socket.close();
	}
}
