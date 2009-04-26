package edu.cmu.partytracer.network;

import java.util.Vector;

import android.util.Log;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.model.invitation.UserSingleton;

public class MessageHandler {

	private static String HANDLER_TAG = "Message Handler";
	
	public static void forward(Vector<Object> data)
	{
		String type = (String) data.get(0);
		if(type == Protocol.TYPE_InvitationBean)
		{
			Log.d(HANDLER_TAG, "Processing Invitation Bean");
			
			Invitation newInvite = Invitation.fromInvitationBean((InvitationBean) data.get(1));
			if(!UserSingleton.getUser().isInvitedTo(newInvite))
				UserSingleton.getUser().addInvite(newInvite);
		}
		else if(type == Protocol.TYPE_VoteBean)
		{
			Log.d(HANDLER_TAG, "Processing Vote Bean");
			VoteBean vb = (VoteBean) data.get(1);
			UserSingleton.getUser().addVote(vb);
			UserSingleton.getUser().activateEvent(Integer.valueOf(vb.getPartyId()));
			
			String inviteName = UserSingleton.getUser().getNameOf(Integer.valueOf(vb.getPartyId()));
			ComWrapper.getComm().addAlert("Event " + inviteName + " has finished voting");
		}
	}
}
