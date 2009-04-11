package project.invite;

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

	public static String INVITE = "invite";
	public static String ACTIVE = "active";
	public static String DATA = "data";
	public static String IID = "id";
	public static String INVITE_LIST = "inviteList";
	public static String SENDER = "sender";
	public static String TIMEOUT = "timeout";
	public static String VOTE_DATA = "voteData";
	
	public static String VOTING = "voting";
	public static String VOTER_LIST = "voters";
	
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
        	events[i].addView(inviteButtons[i]);
        	eventList.addView(events[i]);
        }
        
        setContentView(eventList);
	}

	private Invitation[] getInvitesFromIntent(Intent iData)
	{
		ArrayList<Invitation> inviteArray = new ArrayList<Invitation>();
        int item = 0;
        while(this.getIntent().hasExtra(INVITE + item))
        {
        	Bundle inviteData = this.getIntent().getBundleExtra(INVITE + item);
        	InvitationBean ib = new InvitationBean();
        	
        	ib.setActive(inviteData.getBoolean(ACTIVE));
        	ib.setData(inviteData.getStringArray(DATA));
        	ib.setId(inviteData.getInt(IID));
        	ib.setInviteList(inviteData.getIntArray(INVITE_LIST));
        	ib.setSender(inviteData.getInt(SENDER));
        	ib.setTimeout(inviteData.getFloat(TIMEOUT));
        	ib.setVoteData(inviteData.getStringArray(VOTE_DATA));
        		
        	inviteArray.add(Invitation.fromInvitationBean(ib));
        }

        Invitation[] myInvites = (Invitation[]) inviteArray.toArray();
        return myInvites;
	}
}
