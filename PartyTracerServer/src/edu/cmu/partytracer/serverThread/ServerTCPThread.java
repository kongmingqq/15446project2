package edu.cmu.partytracer.serverThread;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Vector;

import edu.cmu.partytracer.dataProcessor.DataParser;



/**
 * Main Thread, receiving the message from the Android nodes
 * 
 */
public class ServerTCPThread extends Thread {
	Socket clientRequest;
	ServerSocket serverSocket;

	/**
	 * receive thread from Main process
	 * 
	 * @param s
	 *            port number
	 */
	public ServerTCPThread(int s) {
		ServerSocket rServer = null;
		try {
			rServer = new ServerSocket(s);
			System.out.println("Welcome to the server: " + new Date());
			System.out.println("Port: " + rServer.getLocalPort());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		this.serverSocket = rServer;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		ObjectInputStream reader;
		boolean done = false;
		while (!done) {
			try {
				this.clientRequest = this.serverSocket.accept();
				String clientIPAddress = clientRequest.getInetAddress().toString().substring(1);
				System.out.println("New connection accepted " + clientIPAddress + ":" + clientRequest.getPort());
				if (clientRequest != null) {
					reader = new ObjectInputStream(clientRequest.getInputStream());
					Vector<Object> input = (Vector<Object>)reader.readObject();
					System.out.println("input: "+input.getClass());
//					ObjectOutputStream out = new ObjectOutputStream(clientRequest.getOutputStream());
//					out.writeObject("************************************");
					DataParser.parseMsg(input, clientIPAddress,clientRequest);
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				} else {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} catch (ClassNotFoundException e){
				System.out.println(e.getMessage());
			}
		}    
		try {
			clientRequest.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
}