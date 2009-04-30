package edu.cmu.partytracer.activity.invitation.testing;

import java.util.ArrayList;
import java.util.Vector;

import android.util.Log;
import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.network.AbstractComm;
import edu.cmu.partytracer.network.MessageHandler;

public class DummyCommunicator extends AbstractComm{

	private static String NET_TAG = "Simulated Communicator";
	private String storedNumber;
	private ArrayList<Invitation> database;
	
	public String getMyNumber() {
		return storedNumber;
	}

	public void send(String identifier, Object obj) {
		Log.d(NET_TAG, "Received a message");
		Log.d(NET_TAG, "Identifier is " + identifier);
		
		if(identifier.equals(Protocol.TYPE_InvitationBean))
		{
			InvitationBean ib = (InvitationBean)obj;
			sendInvite(ib);
		}
		else if(identifier.equals(Protocol.TYPE_VoteBean))
		{
			VoteBean vb = (VoteBean)obj;
			Log.d(NET_TAG, "Received a vote for invite " + vb.getPartyId());
			addVote(vb);
		}
	}

	public void initNumber(String myNumber) {
		storedNumber = myNumber;
	}
	
	private void addVote(VoteBean vb)
	{
		for(int i=0; i<database.size(); i++)
		{
			Log.d(NET_TAG, "The vote is for " + Integer.valueOf(vb.getPartyId()));
			Log.d(NET_TAG, "This invite is " + database.get(i).getId());
			if(Integer.valueOf(vb.getPartyId()) == database.get(i).getId())
			{
				database.get(i).addVotes(vb);

				VoteBean sendBack = database.get(i).getVotingInfo();
				MessageHandler.forward(sendBack);
			}
		}
		
	}
	
	private void sendInvite(InvitationBean ib)
	{
		if(database == null) database = new ArrayList<Invitation>(); 
		
		Log.d(NET_TAG, "Sending invite to user");
		Invitation invite = Invitation.fromInvitationBean(ib);
		Vector<Object> sendData = new Vector<Object>();
		
		Log.d(NET_TAG, "Invitation Constructed");
		invite.assignId(TestDataGenerator.getNewId());
		ib = invite.toInvitationBean();
		database.add(invite);
		
		Log.d(NET_TAG, "Bean Constructed");
		sendData.add(Protocol.TYPE_InvitationBean);
		sendData.add(ib);
		
		Log.d(NET_TAG, "Passing to message handler");
		MessageHandler.forward(sendData);
	}
}
