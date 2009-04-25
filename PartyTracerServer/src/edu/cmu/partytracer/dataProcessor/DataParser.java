package edu.cmu.partytracer.dataProcessor;

import java.util.HashMap;
import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.serverThread.ServerSingleton;

public class DataParser {
	public static void parseMsg(Vector<Object> input, String clientIPAddress) {
		String msgType = input.get(0).toString();
		if (msgType.equals("INIT")) {
			// format is <INIT, invitationBean>
			DataDispatcher.storeInvitationMsg((InvitationBean)input.get(1));
		} else if (msgType.equals("REQ")) {
			// format is <"REQ", partyID>
			ClientCommunicator.sendVoteOptionInformation(input.get(1).toString(), clientIPAddress);
		} else if (msgType.equals("VOTE")) {
			// format is <VOTE, voteBean>
			DataDispatcher.storeVoteMsg((VoteBean)input.get(1), clientIPAddress);
		} else {
			System.out.println("throw away unknown message");
		}
	}

	// Currently only support att and verison
	public static String[] parseInvitationList(InvitationBean invitationBean) {
		int providerNum = 1;
		String[] resultSet = new String[invitationBean.getInviteList().length*providerNum+1];
		int counter = 0;
		resultSet[counter++] = invitationBean.getSender()+"@txt.att.net";
		for (String eachPerson : invitationBean.getInviteList()){
//			resultSet[counter++] = eachPerson+"@vtext.com";
			resultSet[counter++] = eachPerson+"@txt.att.net";
		}
		return resultSet;
	}
	
	public static String getResult(String partyID){
		String result = new String();
		int largestVote = 0;
		HashMap<String, Integer> voteMap = ServerSingleton.getInstance().getVoteMap(partyID);
		for (String eachOption : voteMap.keySet()){
			if (voteMap.get(eachOption) >= largestVote)
				result = eachOption;
		}
		return result;
	}
}
