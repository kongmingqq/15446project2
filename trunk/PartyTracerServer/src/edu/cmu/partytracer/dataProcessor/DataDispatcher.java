package edu.cmu.partytracer.dataProcessor;

import java.net.Socket;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.LocationBean;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.controller.PartyTimer;
import edu.cmu.partytracer.controller.VoteTimer;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.serverThread.ServerSingleton;
import edu.cmu.partytracer.serverThread.ServerUDPThread.ServerCacheQueue;

public class DataDispatcher {
	public static void storeInvitationMsg(InvitationBean invitationBean) {
		String partyID = ServerSingleton.getInstance().getModel().getInvitationDAO().storeInvitationData(invitationBean);
		ServerSingleton.getInstance().setCurStatus(partyID, "GET_FIRST_INVITATION");
		ServerSingleton.getInstance().setInvitationBean(partyID, invitationBean);
		if (ServerSingleton.getInstance().partyTimerThreadMap.get(partyID)==null){
			ServerSingleton.getInstance().partyTimerThreadMap.put(partyID, new PartyTimer(invitationBean.getPartyTime(),partyID));
		}
		if (ServerSingleton.getInstance().voteTimerThreadMap.get(partyID)==null){
			ServerSingleton.getInstance().voteTimerThreadMap.put(partyID, new VoteTimer(invitationBean.getTimeout(),partyID));
		}
		new VoteTimer(invitationBean.getTimeout(), partyID);
		try {
			System.out.println("Insert invitation bean successful!");
			SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
			String[] invitationList = DataParser.parseInvitationList(invitationBean);
//			for(String each : invitationList){
//				System.out.println("phone number is: "+each);
//			}
			smtpMailSender.postMail(invitationList, "You have a new Invitation", partyID, "partytracer@gmail.com");
			System.out.println("Sucessfully Sent mail to All Users");
			ServerSingleton.getInstance().setCurStatus(partyID, "SEND_FIRST_INVITATION");
			if (ServerSingleton.getInstance().voteProcessMap.get(partyID) == null){
				invitationBean.setId(partyID);
				ServerSingleton.getInstance().voteProcessMap.put(partyID, Invitation.fromInvitationBean(invitationBean));
//						new Invitatin(Integer.valueOf(partyID), invitationBean.getData()[0],invitationBean.getData()[1],invitationBean.getSender()));
			}

		} catch (Exception e) {
			System.out.println("Error in storeInvitationMsg: " + e.getMessage());
		}
	}
	/**
	 * Collect the vote information and store in the hashmap
	 * 
	 * @param voteBean
	 */
	public static void storeVoteMsg(VoteBean voteBean, String clientIPAddress, Socket clientRequest) {
//		String partyID = voteBean.getPartyID();
//		String voteOption = voteBean.getMyVote();
//		ServerSingleton.getInstance().increaseVote(voteBean.getPartyID(), voteOption);
//		ServerSingleton.getInstance().saveClientAddress(voteBean.getPartyID(), clientIPAddress);
//		if (ServerSingleton.getInstance().getInvitationBean(partyID).getInviteList().length+1 == ServerSingleton.getInstance().getClientList(partyID).size()){
//			ClientCommunicator.sendVoteResult(partyID);
//		}
		Invitation curVote = ServerSingleton.getInstance().voteProcessMap.get(voteBean.getPartyId());
//		System.out.println("Party ID:"+voteBean.getPartyId()+"\nCurVote is: "+curVote);
		curVote.addVotes(voteBean);
//		for (String aa : ServerSingleton.getInstance().voteProcessMap.get(voteBean.getPartyId()).getVotingInfo().getData())
//			System.out.println("******Vote added:"+aa);
		ServerSingleton.getInstance().voteProcessMap.put(voteBean.getPartyId(), curVote);
	}
	
	/**
	 * Store the location information in the queue, and send to the clients who are not in sleep mode
	 * @param loc
	 * @param clientIPAddress
	 */
	public static void storeLocationMsg(LocationBean loc, String clientIPAddress) {
		//test
		if (ServerSingleton.getInstance().partyTimerThreadMap.get(loc.getPartyID())==null){
			ServerSingleton.getInstance().partyTimerThreadMap.put(loc.getPartyID(), new PartyTimer(30,loc.getPartyID()));
		}
		//test
		ServerSingleton.getInstance().addToLocationQueue(loc.getPartyID(), loc);
		if (!loc.isSleepMode()){
			if (ServerSingleton.getInstance().getLocationCache(loc.getPartyID())==null){
//				System.out.println("First insert!");
				ServerCacheQueue serverCacheQueue= ServerSingleton.getInstance().getLocationQueue(loc.getPartyID());
				ServerSingleton.getInstance().setLocationCache(loc.getPartyID(), serverCacheQueue.dequeueLocationBatch(serverCacheQueue.size()/2));
			}
			System.out.println("Client is not in sleep mode, send the information");
			ClientCommunicator.sendAggregatedLocation(loc.getPartyID(), clientIPAddress, loc);
		}
	}
	public static void queryResult(String partyID, Socket clientRequest) {
		if ((ServerSingleton.getInstance().voteProcessMap.get(partyID).numVotedUsers() == ServerSingleton.getInstance().getClientList(partyID).size()+1) ||ServerSingleton.getInstance().getCurStatus(partyID).equals("RESULT_SEND_TIMEOUT")){
			ClientCommunicator.sendVoteResult(partyID, clientRequest);
		}else{
			ClientCommunicator.sendVoteStatus(partyID, clientRequest);
		}
	}

}
