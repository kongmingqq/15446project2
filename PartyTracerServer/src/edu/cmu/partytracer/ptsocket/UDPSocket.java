package edu.cmu.partytracer.ptsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import edu.cmu.partytracer.bean.Protocol;
/**
 * UDP version of PTSocket implementation, untested
 * @author km
 *
 */
public class UDPSocket implements PTSocket {
	static int DEFAULT_BYTE_SIZE = 1024;
	InetAddress destIp = null;
	int destPort = -1;
	int port = -1;
	DatagramSocket socket;
	/**
	 * send only
	 * @param destAddr
	 * @param destPort
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public UDPSocket(String destAddr, int destPort) throws SocketException, UnknownHostException {
		socket = new DatagramSocket();
		destIp = InetAddress.getByName(destAddr);
		this.destPort = destPort;
	}
	/**
	 * receive only
	 * @param port
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public UDPSocket(int port) throws SocketException, UnknownHostException {
		socket = new DatagramSocket(port);
		this.port = port;
	}

	public void sendObject(Object obj) throws IOException {
		byte[] bs = Util.objToBytes(obj);
		DatagramPacket pack = new DatagramPacket(bs,bs.length,destIp,destPort);
		socket.send(pack);
	}
	
	public Object receiveObject() throws IOException {
		byte[] bs = new byte[DEFAULT_BYTE_SIZE];
		DatagramPacket p = new DatagramPacket(bs,bs.length);
		socket.receive(p);
		return Util.bytesToObj(p.getData());
	}
	
	public void close() {
		socket.close();
	}
	
	public void setTimeout(int t) throws SocketException {
		socket.setSoTimeout(t);
	}
}
