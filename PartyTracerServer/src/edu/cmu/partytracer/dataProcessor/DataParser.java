package edu.cmu.partytracer.dataProcessor;

import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;

public class DataParser {
	public static void parseMsg(Vector<Object> input) {
		String msgType = input.get(0).toString();
		if (msgType.equals("INIT")) {
			DataDispatcher.storeInvitationMsg((InvitationBean)input.get(1));
		} else if (msgType.equals("VOTE")) {

		} else if (msgType.equals("TERM")) {

		} else {
			System.out.println("throw away unknown message");
		}
	}
}
