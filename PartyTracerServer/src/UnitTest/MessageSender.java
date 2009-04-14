package UnitTest;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import DataBeans.InvitationBean;

public class MessageSender {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector<Object> myVector = new Vector<Object>();
		InvitationBean myBean = new InvitationBean();
		myBean.setSampleData("This is a test");
		myBean.setTime(System.currentTimeMillis());
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
