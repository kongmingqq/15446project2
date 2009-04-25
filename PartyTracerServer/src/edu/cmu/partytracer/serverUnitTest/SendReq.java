package edu.cmu.partytracer.serverUnitTest;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;

public class SendReq {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//send req
		try {
			Vector<Object> myVector = new Vector<Object>();
			myVector.add("REQ");
			myVector.add("1240682470281");
			Socket mySocket = new Socket("localhost", 15446);
			ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
			out.writeObject(myVector);
			out.close();
			Thread.sleep(5000);
			mySocket.close();
		} catch (Exception e) {
			System.out.println("Error" + e.getMessage());
		}
	}
}
