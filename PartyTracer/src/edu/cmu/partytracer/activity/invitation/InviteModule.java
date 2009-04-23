package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.network.ComWrapper;
import edu.cmu.partytracer.R;
import edu.cmu.partytracer.model.invitation.BundleParser;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.model.invitation.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

public class InviteModule extends Activity implements View.OnClickListener{
	
	private User thisUser;
	
	static int CREATE_INVITE = 1;
	static int VIEW_VOTES = 2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Get this user's phone number and store it in the singleton class so that other methods can access it
    	TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    	ComWrapper.getComm().initNumber(tm.getDeviceId());
    	
    	thisUser = new User();
    	    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite);
        
        Button createInvite = (Button) findViewById(R.id.create);
        Button viewActive = (Button) findViewById(R.id.active);
        Button viewVotes = (Button) findViewById(R.id.vote);
        
        createInvite.setOnClickListener(this);
        viewActive.setOnClickListener(this);
        viewVotes.setOnClickListener(this);
    }
    
	public void onClick(View v) {
		//There are three buttons that the user can click from this screen, and each one launches a new activity
		
		if(v.getId() == R.id.create)
		{
			//The first one is simple to launch: create a new invitation
			Intent create = new Intent(this, edu.cmu.partytracer.activity.invitation.CreationDialog.class);
			startActivityForResult(create, CREATE_INVITE);
		}
		else if(v.getId() == R.id.active)
		{
			//The second is a dialog to view all invitations which are "active"
			Intent viewActive = new Intent(this, edu.cmu.partytracer.activity.invitation.ViewInvitesDialog.class);

			//The same dialog is launched whether we're looking at active or vote invites, so let the dialog know 
			// which one was selected
			viewActive.putExtra("active", true);
			
			//Get a list of each of this user's active invites, and store each one in the intent used to
			// launch the new activity
			Invitation[] myInvites = thisUser.getMyInvites();
			int item = 0;
			for(int i=0; i<myInvites.length; i++)
			{
				if(myInvites[i].isActive())
				{
					InvitationBean ib = myInvites[i].toInvitationBean();
					viewActive.putExtra(ViewInvitesDialog.INVITE + item, constructInviteBundle(ib));
					item++;
				}
			}
			
			//Since we don't need to return anything, just start the activity as normal
			startActivity(viewActive);
		}
		else if(v.getId() == R.id.vote)
		{
			//This launches a dialog to vote on any voting invites the user might have
			Intent viewVotes = new Intent(this, edu.cmu.partytracer.activity.invitation.ViewInvitesDialog.class);
			viewVotes.putExtra("active", false);
			
			//Get a list of each of this user's voting invites, and store each one in the intent used to
			// launch the new activity 
			Invitation[] myInvites = thisUser.getMyInvites();
			int item = 0;
			for(int i=0; i<myInvites.length; i++)
			{
				if(!myInvites[i].isActive())
				{
					InvitationBean ib = myInvites[i].toInvitationBean();
					viewVotes.putExtra(ViewInvitesDialog.INVITE + item, constructInviteBundle(ib));
					item++;
				}
			}
			
			//We'll need to return some data representing the votes that the user chose
			startActivityForResult(viewVotes, VIEW_VOTES);
		}
	}
	
	//Bundles up the data in an invitation into a form that can be sent to the view invites dialog
	public static Bundle constructInviteBundle(InvitationBean ib)
	{
		Bundle invBundle = new Bundle();
		
		invBundle.putBoolean(ViewInvitesDialog.ACTIVE, ib.getActive());
		invBundle.putStringArray(ViewInvitesDialog.DATA, ib.getData());
		invBundle.putInt(ViewInvitesDialog.IID, ib.getId());
		invBundle.putStringArray(ViewInvitesDialog.INVITE_LIST, ib.getInviteList());
		invBundle.putString(ViewInvitesDialog.SENDER, ib.getSender());
		invBundle.putFloat(ViewInvitesDialog.TIMEOUT, ib.getTimeout());
		invBundle.putStringArray(ViewInvitesDialog.VOTE_DATA, ib.getVoteData());
		
		return invBundle;
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
        	if (requestCode == CREATE_INVITE) 
        	{
        		//The user just created a new invitation, so take the information from the CreateInvites
        		// activity and create an invitation object from it
        		InvitationBean ib = new InvitationBean();
        		
        		//First, create the bean that will be sent to the server
        		Bundle eventProps = data.getBundleExtra(CreationDialog.EVENT_DETAILS);
        		String[] voteData = new String[0];
        		String[] eventData = BundleParser.parseEventData(eventProps);
        		
        		//fill in all the data fields for the invitation bean
        		ib.setSender(thisUser.getNumber());
        		ib.setVoteData(voteData);
        		ib.setData(eventData);
        		ib.setId(0);
        		
        		ArrayList<String> invited = data.getStringArrayListExtra(CreationDialog.INVITED_LIST);
        		String[] invitedNumbers = (String[]) invited.toArray();
        		ib.setInviteList(invitedNumbers);
        		
        		ArrayList<String> voteOptions = data.getStringArrayListExtra(Invitation.voterString);
        		ib.setOptions((String[]) voteOptions.toArray());
        		
        		//add the invitation to our user's database and send it off to the server
        		thisUser.addInvite(Invitation.fromInvitationBean(ib));
        		ComWrapper.getComm().send(Protocol.TYPE_InvitationBean, ib);
        	}
	        else if(requestCode == VIEW_VOTES)
	        {
	        	//The user just voted on something
	        	int item = 0;
	        	String[] singleVoter = new String[1];
	        	singleVoter[0] = thisUser.getNumber();
	        	
	        	while(data.hasExtra(ViewInvitesDialog.VOTING + item))
	        	{
		        	Bundle voteProps = data.getBundleExtra(ViewInvitesDialog.VOTING + item);
		        	VoteBean vb = new VoteBean();
		        	
		        	//Extract the information about what the user voted on and send it off
		        	vb.setData(voteProps.getStringArray(ViewInvitesDialog.VOTE_DATA));
		        	vb.setVoters(singleVoter);
		        	vb.setWhichInvite(voteProps.getInt(ViewInvitesDialog.INVITE));
		        	
		        	ComWrapper.getComm().send(Protocol.TYPE_VoteBean, vb);
	        	}
	        }
        }
    }

}
