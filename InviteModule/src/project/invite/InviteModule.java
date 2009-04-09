package project.invite;

import beans.InvitationBean;
import beans.VoteBean;
import simulator.ComWrapper;
import model.Invitation;
import model.User;
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
        setContentView(R.layout.main);
        
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
			Intent create = new Intent(this, dialogs.CreationDialog.class);
			startActivityForResult(create, CREATE_INVITE);
		}
		else if(v.getId() == R.id.active)
		{
			Intent viewActive = new Intent(this, dialogs.ViewInvitesDialog.class);
			viewActive.putExtra("active", true);
			
			Invitation[] myInvites = thisUser.getMyInvites();
			int item = 0;
			for(int i=0; i<myInvites.length; i++)
			{
				if(myInvites[i].isActive())
				{
					InvitationBean ib = myInvites[i].toInvitationBean();
					viewActive.putExtra("invite" + item, constructInviteBundle(ib));
					item++;
				}
			}
			
			startActivity(viewActive);
		}
		else if(v.getId() == R.id.vote)
		{
			Intent viewVotes = new Intent(this, dialogs.ViewInvitesDialog.class);
			viewVotes.putExtra("active", false);
			
			Invitation[] myInvites = thisUser.getMyInvites();
			int item = 0;
			for(int i=0; i<myInvites.length; i++)
			{
				if(!myInvites[i].isActive())
				{
					InvitationBean ib = myInvites[i].toInvitationBean();
					viewVotes.putExtra("invite" + item, constructInviteBundle(ib));
					item++;
				}
			}
			
			startActivityForResult(viewVotes, VIEW_VOTES);
		}
	}
	
	private Bundle constructInviteBundle(InvitationBean ib)
	{
		Bundle invBundle = new Bundle();
		
		invBundle.putBoolean("active", ib.getActive());
		invBundle.putStringArray("data", ib.getData());
		invBundle.putInt("id", ib.getId());
		invBundle.putIntArray("inviteList", ib.getInviteList());
		invBundle.putInt("sender", ib.getSender());
		invBundle.putFloat("timeout", ib.getTimeout());
		invBundle.putStringArray("voteData", ib.getVoteData());
		
		return invBundle;
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
        	if (requestCode == CREATE_INVITE) 
        	{
        		Bundle eventProps = data.getBundleExtra("details");
        		String eventName = eventProps.getString("name").toString();
        		String eventData = eventProps.getString("description").toString();
        		
        		String[] invited = (String[]) data.getStringArrayListExtra("invited").toArray();
        		
        		Invitation newInvite = new Invitation(ComWrapper.getComm().nextEventId(), eventName, eventData, thisUser.getNumber());
        		for(int i=0; i<invited.length; i++) {
        			newInvite.addInvite(ComWrapper.getComm().lookUp(invited[i]));
        		}
        		
        		thisUser.addInvite(newInvite);
        		ComWrapper.getComm().send(0, newInvite.toInvitationBean());
        	}
	        else if(requestCode == VIEW_VOTES)
	        {
	        	Bundle voteProps = data.getBundleExtra("voting");
	        	VoteBean vb = new VoteBean();
	        	
	        	vb.setData(voteProps.getStringArray("data"));
	        	vb.setVoters(voteProps.getIntArray("voters"));
	        	vb.setWhichInvite(voteProps.getInt("whichInvite"));
	        	
	        	ComWrapper.getComm().send(1, vb);
	        }
        }
    }

}