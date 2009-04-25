package edu.cmu.partytracer.dataProcessor;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.LocationBean;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.controller.PartyTimer;
import edu.cmu.partytracer.serverThread.ServerSingleton;

public class DataDispatcher {
	public static void storeInvitationMsg(InvitationBean invitationBean) {
		String partyID = ServerSingleton.getInstance().getModel().getInvitationDAO().storeInvitationData(invitationBean);
		ServerSingleton.getInstance().setCurStatus(partyID, "GET_FIRST_INVITATION");
		ServerSingleton.getInstance().setInvitationBean(partyID, invitationBean);
		new PartyTimer(invitationBean.getTimeout(), partyID);
		try {
			System.out.println("Insert invitation bean successful!");
			SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
			String[] invitationList = DataParser.parseInvitationList(invitationBean);
			smtpMailSender.postMail(invitationList, "You have a new Invitation", partyID, "partytracer@gmail.com");
			System.out.println("Sucessfully Sent mail to All Users");
			ServerSingleton.getInstance().setCurStatus(partyID, "SEND_FIRST_INVITATION");
		} catch (Exception e) {
			System.out.println("Error in storeInvitationMsg: " + e.getMessage());
		}
	}
	/**
	 * Collect the vote information and store in the hashmap
	 * 
	 * @param voteBean
	 */
	public static void storeVoteMsg(VoteBean voteBean, String clientIPAddress) {
		String partyID = voteBean.getPartyID();
		String voteOption = voteBean.getMyVote();
		ServerSingleton.getInstance().increaseVote(voteBean.getPartyID(), voteOption);
		ServerSingleton.getInstance().saveClientAddress(voteBean.getPartyID(), clientIPAddress);
		if (ServerSingleton.getInstance().getInvitationBean(partyID).getInviteList().length+1 == ServerSingleton.getInstance().getClientList(partyID).size()){
			ClientCommunicator.sendVoteResult(partyID);
		}
	}
	public static void storeLocationMsg(LocationBean loc, String clientIPAddress) {
		ServerSingleton.getInstance().addToLocationQueue(loc.getPartyID(), loc);
		ClientCommunicator.sendAggregatedLocation(loc.getPartyID(), clientIPAddress, loc);
	}

}
