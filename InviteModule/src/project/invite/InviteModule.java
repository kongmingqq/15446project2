package project.invite;

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
			startActivity(viewActive);
		}
		else if(v.getId() == R.id.vote)
		{
			Intent viewVotes = new Intent(this, dialogs.ViewInvitesDialog.class);
			viewVotes.putExtra("active", false);
			startActivityForResult(viewVotes, VIEW_VOTES);
		}
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_INVITE) 
        {
        	if(resultCode == RESULT_OK)
        	{
        		Bundle eventProps = data.getBundleExtra("details");
        		String eventName = eventProps.getString("name").toString();
        		String eventData = eventProps.getString("description").toString();
        		
        		String[] invited = (String[]) data.getStringArrayListExtra("invited").toArray();
        		
        		Invitation newInvite = new Invitation(ComWrapper.getComm().nextEventId(), eventName, eventData, thisUser.getNumber());
        		for(int i=0; i<invited.length; i++) {
        			newInvite.addInvite(ComWrapper.getComm().lookUp(invited[i]));
        		}
        		
//        		String[] inviteData = newInvite.toStringArray();
//        		DataObject sendData = new DataObject(DataObject.INVITATION, inviteData);
//        		ComWrapper.getComm().send(sendData);
        	}
        }
        else if(requestCode == VIEW_VOTES)
        {
        	
        }
    }

}