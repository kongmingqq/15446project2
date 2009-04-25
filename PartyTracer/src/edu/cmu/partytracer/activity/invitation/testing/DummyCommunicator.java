package edu.cmu.partytracer.activity.invitation.testing;

import java.util.Vector;

import android.util.Log;
import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.network.AbstractComm;
import edu.cmu.partytracer.network.MessageHandler;

public class DummyCommunicator extends AbstractComm{

	private static String NET_TAG = "Simulated Communicator";
	private String storedNumber;
	
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
	}

	public void initNumber(String myNumber) {
		storedNumber = myNumber;
	}
	
	private void sendInvite(InvitationBean ib)
	{
		Log.d(NET_TAG, "Sending invite to user");
		Invitation invite = Invitation.fromInvitationBean(ib);
		Vector<Object> sendData = new Vector<Object>();
		
		Log.d(NET_TAG, "Invitation Constructed");
		invite.assignId(TestDataGenerator.getNewId());
		ib = invite.toInvitationBean();
		
		Log.d(NET_TAG, "Bean Constructed");
		sendData.add(Protocol.TYPE_InvitationBean);
		sendData.add(ib);
		
		Log.d(NET_TAG, "Passing to message handler");
		MessageHandler.forward(sendData);
	}
}
