package edu.cmu.partytracer.network;

import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.model.invitation.UserSingleton;

public class MessageHandler {

	public static void forward(Vector<Object> data)
	{
		String type = (String) data.get(0);
		if(type == Protocol.TYPE_InvitationBean)
		{
			Invitation newInvite = Invitation.fromInvitationBean((InvitationBean) data.get(1));
			if(!UserSingleton.getUser().isInvitedTo(newInvite))
				UserSingleton.getUser().addInvite(newInvite);
		}
		else if(type == Protocol.TYPE_VoteBean)
		{
			VoteBean vb = (VoteBean) data.get(1);
			UserSingleton.getUser().addVote(vb);
		}
	}
}
