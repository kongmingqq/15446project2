package edu.cmu.partytracer.ptsocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * TCP version of PTSocket implementation, tested
 * @author km
 *
 */
public class TCPSocket implements PTSocket {
	InetAddress destIp = null;
	int destPort = -1;
	int port = -1;
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	//for receiving or server
	public TCPSocket(int port) throws IOException {
		this.port = port;
		ServerSocket ss = new ServerSocket(port);
		socket = ss.accept();
		//System.out.println("port is "+socket.getLocalPort());
		ss.close();
		//System.out.println("port is "+socket.getLocalPort());
		//System.out.println("port is "+socket.getLocalAddress());
		destPort = socket.getPort();
		destIp = socket.getInetAddress();
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	
	//for sending or client
	public TCPSocket(String destAddr, int destPort) throws IOException {
		destIp = InetAddress.getByName(destAddr);
		socket = new Socket(destIp, destPort);
		port = socket.getLocalPort();
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
