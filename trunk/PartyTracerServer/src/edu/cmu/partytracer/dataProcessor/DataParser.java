package edu.cmu.partytracer.dataProcessor;

import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.VoteBean;

public class DataParser {
	public static void parseMsg(Vector<Object> input, String clientIPAddress) {
		String msgType = input.get(0).toString();
		if (msgType.equals("INIT")) {
			DataDispatcher.storeInvitationMsg((InvitationBean)input.get(1));
		} else if (msgType.equals("REQ")) {
			// format is <"REQ", partyID>
			DataDispatcher.sendVoteOptionInformation(input.get(1).toString(), clientIPAddress);
		} else if (msgType.equals("VOTE")) {
			DataDispatcher.storeVoteMsg((VoteBean)input.get(1), clientIPAddress);
		} else {
			System.out.println("throw away unknown message");
		}
	}

	// Currently only support att and verison
	public static String[] parseInvitationList(String[] inviteList) {
		String[] resultSet = new String[inviteList.length*2];
		int counter = 0;
		for (String eachPerson : inviteList){
			resultSet[counter++] = eachPerson+"@vtext.com";
			resultSet[counter++] = eachPerson+"@txt.att.net";
		}
		return resultSet;
	}
}
