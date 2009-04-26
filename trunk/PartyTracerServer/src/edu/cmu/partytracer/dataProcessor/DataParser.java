package edu.cmu.partytracer.dataProcessor;

import java.util.HashMap;
import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.serverThread.ServerSingleton;

public class DataParser {
	/**
	 * Parse the data come from the client and dispatch to different method and class
	 * @param input
	 * @param clientIPAddress
	 */
	public static void parseMsg(Vector<Object> input, String clientIPAddress) {
		String msgType = input.get(0).toString();
		if (msgType.equals(Protocol.TYPE_InvitationBean) ) {
			// format is <INIT, invitationBean>
			DataDispatcher.storeInvitationMsg((InvitationBean)input.get(1));
		} else if (msgType.equals(Protocol.TYPE_Request) && ServerSingleton.getInstance().curStatus.get(input.get(1).toString())!= null && ServerSingleton.getInstance().curStatus.get(input.get(1).toString()).equals("SEND_FIRST_INVITATION")) {
			// format is <"REQ", partyID>
			ClientCommunicator.sendVoteOptionInformation(input.get(1).toString(), clientIPAddress);
		} else if (msgType.equals(Protocol.TYPE_VoteBean) && ServerSingleton.getInstance().curStatus.get(input.get(1).toString())!= null && ServerSingleton.getInstance().curStatus.get(input.get(1).toString()).equals("SENDING_OPTIONS")) {
			// format is <VOTE, voteBean>
			DataDispatcher.storeVoteMsg((VoteBean)input.get(1), clientIPAddress);
		} else {
			System.out.println("throw away unknown message");
		}
	}

	/**
	 * prepare the sending address for the sms sender, right now it supports att and verison
	 * 
	 * @param invitationBean
	 * @return the 
	 */
	public static String[] parseInvitationList(InvitationBean invitationBean) {
		//number of provider
		int providerNum = 2;
		String[] resultSet = new String[invitationBean.getInviteList().length*providerNum+1];
		int counter = 0;
		// we assume the 
		resultSet[counter++] = invitationBean.getSender().substring(1)+"@txt.att.net";
		for (String eachPerson : invitationBean.getInviteList()){
			resultSet[counter++] = eachPerson.substring(1)+"@vtext.com";
			resultSet[counter++] = eachPerson.substring(1)+"@txt.att.net";
		}
		return resultSet;
	}
	
//	public static String getResult(String partyID){
//		String result = new String();
//		int largestVote = 0;
//		HashMap<String, Integer> voteMap = ServerSingleton.getInstance().getVoteMap(partyID);
//		for (String eachOption : voteMap.keySet()){
//			if (voteMap.get(eachOption) >= largestVote)
//				result = eachOption;
//		}
//		return result;
//	}
}
