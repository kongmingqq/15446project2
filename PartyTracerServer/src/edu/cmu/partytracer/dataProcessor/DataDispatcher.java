package edu.cmu.partytracer.dataProcessor;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.serverThread.ServerSingleton;

public class DataDispatcher {
	public static void storeInvitationMsg(InvitationBean invitationBean) {
		String partyID = ServerSingleton.getInstance().getModel().getInvitationDAO().storeInvitationData(invitationBean);
		ServerSingleton.getInstance().setCurStatus(partyID, "GET_FIRST_INVITATION");
		ServerSingleton.getInstance().setInvitationBean(partyID, invitationBean);
		try {
			System.out.println("Insert invitation bean successful!");
			SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
			String[] invitationList = DataParser.parseInvitationList(invitationBean.getInviteList());
			smtpMailSender.postMail(invitationList, "You have a new Invitation", partyID, "partytracer@gmail.com");
			System.out.println("Sucessfully Sent mail to All Users");
			ServerSingleton.getInstance().setCurStatus(partyID, "SEND_FIRST_INVITATION");
		} catch (Exception e) {
			System.out.println("Error in send mail: " + e.getMessage());
		}
	}

	public static void storeVoteMsg(VoteBean voteBean, String clientIPAddress) {
		
	}

	public static void sendVoteOptionInformation(String partyID, String clientIPAddress) {
		try{
			Vector<Object> sendVector = new Vector<Object>();
			VoteBean voteBean = new VoteBean();
			String dataType = "VOTEINFO";
			sendVector.add(dataType);
			String[] optionList = ServerSingleton.getInstance().getInvitationBean(partyID).getOptions();
			voteBean.setData(optionList);
			sendVector.add(voteBean);
			Socket sendSocket = new Socket(clientIPAddress, ServerSingleton.clientPort);
			ObjectOutputStream out = new ObjectOutputStream(sendSocket.getOutputStream());
			out.writeObject(sendVector);
			out.close();
			Thread.sleep(5000);
			sendSocket.close();
		}catch(Exception e){
			System.out.println("Error in sending vote info: "+e.getMessage());
		}
	}
}
