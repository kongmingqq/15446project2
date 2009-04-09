package dialogs;

import java.util.ArrayList;

import beans.InvitationBean;

import model.Invitation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewInvitesDialog extends Activity{

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isActive = this.getIntent().getBooleanExtra("active", true);
        String buttonText = "View Details";
        if(!isActive) buttonText = "Vote";
        
        Invitation[] myInvites = getInvitesFromIntent(this.getIntent());
        
        LinearLayout eventList = new LinearLayout(this);
        eventList.setOrientation(LinearLayout.VERTICAL);
        LinearLayout[] events = new LinearLayout[myInvites.length];
        TextView[] inviteNames = new TextView[myInvites.length];
        Button[] inviteButtons = new Button[myInvites.length];
        
        for(int i=0; i<myInvites.length; i++)
        {
        	events[i] = new LinearLayout(this);
        	
        	inviteNames[i] = new TextView(this);
        	inviteNames[i].setText(myInvites[i].getTitle());
        	
        	inviteButtons[i] = new Button(this);
        	inviteButtons[i].setText(buttonText);
        	
        	events[i].addView(inviteNames[i]);
        }
        
        setContentView(eventList);
	}

	private Invitation[] getInvitesFromIntent(Intent iData)
	{
		ArrayList<Invitation> inviteArray = new ArrayList<Invitation>();
        int item = 0;
        while(this.getIntent().hasExtra("invite" + item))
        {
        	Bundle inviteData = this.getIntent().getBundleExtra("invite" + item);
        	InvitationBean ib = new InvitationBean();
        	
        	ib.setActive(inviteData.getBoolean("active"));
        	ib.setData(inviteData.getStringArray("data"));
        	ib.setId(inviteData.getInt("id"));
        	ib.setInviteList(inviteData.getIntArray("inviteList"));
        	ib.setSender(inviteData.getInt("sender"));
        	ib.setTimeout(inviteData.getFloat("timeout"));
        	ib.setVoteData(inviteData.getStringArray("voteData"));
        		
        	inviteArray.add(Invitation.fromInvitationBean(ib));
        }

        Invitation[] myInvites = (Invitation[]) inviteArray.toArray();
        return myInvites;
	}
}
