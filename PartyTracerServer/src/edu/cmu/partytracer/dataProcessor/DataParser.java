package edu.cmu.partytracer.dataProcessor;

import java.net.Socket;
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
	public static void parseMsg(Vector<Object> input, String clientIPAddress, Socket clientRequest) {
		String msgType = input.get(0).toString();
		if (msgType.equals(Protocol.TYPE_InvitationBean) ) {
			// format is <INIT, invitationBean>
			DataDispatcher.storeInvitationMsg((InvitationBean)input.get(1));
			//TODO: replace with commented for the real case
//		} else if (msgType.equals(Protocol.TYPE_Request)&& ServerSingleton.getInstance().curStatus.get(input.get(1).toString())!= null && ServerSingleton.getInstance().curStatus.get(input.get(1).toString()).equals("SEND_FIRST_INVITATION") ) {
		} else if (msgType.equals(Protocol.TYPE_Request) ) {
			// format is <REQ, partyID>
			ClientCommunicator.sendVoteOptionInformation(input.get(1).toString(), clientIPAddress, clientRequest);
		} else if (msgType.equals(Protocol.TYPE_VoteBean)) {
			// format is <VOTE, voteBean>
			DataDispatcher.storeVoteMsg((VoteBean)input.get(1), clientIPAddress, clientRequest);
		} else if (msgType.equals(Protocol.TYPE_ResultRequest)){
			DataDispatcher.queryResult(input.get(1).toString(), clientRequest);
		}
		else {
			System.out.println("throw away unknown message"+input.get(0));
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
