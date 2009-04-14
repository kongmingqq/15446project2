package edu.cmu.partytracer.serverUnitTest;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;


public class MessageSender {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector<Object> myVector = new Vector<Object>();
		InvitationBean myBean = new InvitationBean();
		myBean.setTimeout((float)20.00);
		myBean.setId(192);
		myVector.add("Invitiation ID 100009");
		myVector.add(myBean);
		while (true) {
			try {
				Socket mySocket = new Socket("localhost", 10004);
				ObjectOutputStream out = new ObjectOutputStream(mySocket
						.getOutputStream());
				out.writeObject(myVector);
				out.close();
				Thread.sleep(5000);
				mySocket.close();
			} catch (Exception e) {
				System.out.println("Error" + e.getMessage());
			}
		}
	}

}
