package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.network.ComWrapper;
import edu.cmu.partytracer.R;
import edu.cmu.partytracer.model.invitation.BundleParser;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.model.invitation.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InviteModule extends Activity implements View.OnClickListener{
	
	private User thisUser;
	
	static int CREATE_INVITE = 1;
	static int VIEW_VOTES = 2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
		if(v.getId() == R.id.create)
		{
			Intent create = new Intent(this, edu.cmu.partytracer.activity.invitation.CreationDialog.class);
			create.putExtra("number", thisUser.getNumber());
			
			startActivityForResult(create, CREATE_INVITE);
		}
		else if(v.getId() == R.id.active)
		{
			Intent viewActive = new Intent(this, edu.cmu.partytracer.activity.invitation.ViewInvitesDialog.class);
			viewActive.putExtra("active", true);
			
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
			
			startActivity(viewActive);
		}
		else if(v.getId() == R.id.vote)
		{
			Intent viewVotes = new Intent(this, edu.cmu.partytracer.activity.invitation.ViewInvitesDialog.class);
			viewVotes.putExtra("active", false);
			
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
			
			startActivityForResult(viewVotes, VIEW_VOTES);
		}
	}
	
	private Bundle constructInviteBundle(InvitationBean ib)
	{
		Bundle invBundle = new Bundle();
		
		invBundle.putBoolean(ViewInvitesDialog.ACTIVE, ib.getActive());
		invBundle.putStringArray(ViewInvitesDialog.DATA, ib.getData());
		invBundle.putInt(ViewInvitesDialog.IID, ib.getId());
		invBundle.putIntArray(ViewInvitesDialog.INVITE_LIST, ib.getInviteList());
		invBundle.putInt(ViewInvitesDialog.SENDER, ib.getSender());
		invBundle.putFloat(ViewInvitesDialog.TIMEOUT, ib.getTimeout());
		invBundle.putStringArray(ViewInvitesDialog.VOTE_DATA, ib.getVoteData());
		
		return invBundle;
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
        	if (requestCode == CREATE_INVITE) 
        	{
        		InvitationBean ib = new InvitationBean();
        		Bundle eventProps = data.getBundleExtra(CreationDialog.EVENT_DETAILS);
        		String[] voteData = new String[0];
        		String[] eventData = BundleParser.parseEventData(eventProps);
        		
        		ib.setSender(thisUser.getNumber());
        		ib.setVoteData(voteData);
        		ib.setData(eventData);
        		ib.setId(0);
        		
        		ArrayList<String> invited = data.getStringArrayListExtra(CreationDialog.INVITED_LIST);
        		int[] invitedNumbers = BundleParser.parseInvitedNumbers(invited);
        		ib.setInviteList(invitedNumbers);
        		
        		ArrayList<String> voteOptions = data.getStringArrayListExtra(Invitation.voterString);
        		ib.setOptions((String[]) voteOptions.toArray());
        		
        		thisUser.addInvite(Invitation.fromInvitationBean(ib));
        		ComWrapper.getComm().send(0, ib);
        	}
	        else if(requestCode == VIEW_VOTES)
	        {
	        	Bundle voteProps = data.getBundleExtra(ViewInvitesDialog.VOTING);
	        	VoteBean vb = new VoteBean();
	        	
	        	vb.setData(voteProps.getStringArray(ViewInvitesDialog.VOTE_DATA));
	        	vb.setVoters(voteProps.getIntArray(ViewInvitesDialog.VOTER_LIST));
	        	vb.setWhichInvite(voteProps.getInt(ViewInvitesDialog.INVITE));
	        	
	        	ComWrapper.getComm().send(1, vb);
	        }
        }
    }

}
