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
		// send init
		try {
			Vector<Object> myVector = new Vector<Object>();
			InvitationBean myBean = new InvitationBean();
			myBean.setTimeout(999999);
			myBean.setSender("Sender_xiaojian");
			String[] inviteList = { "4125892943", "4129994538", "5852084274"};
			myBean.setInviteList(inviteList);
			String[] options = { "OptionCMU", "optionHBH" };
			myBean.setOptions(options);
			myVector.add("INIT");
			myVector.add(myBean);
			Socket mySocket = new Socket("128.237.231.245", 1544);
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
