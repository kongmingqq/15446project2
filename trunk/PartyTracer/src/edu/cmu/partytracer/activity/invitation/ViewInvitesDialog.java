package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;

import edu.cmu.partytracer.bean.InvitationBean;

import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewInvitesDialog extends Activity implements View.OnClickListener{

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
	
	private LinearLayout eventList;
	private boolean isActive;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewinvites);
        
        isActive = this.getIntent().getBooleanExtra("active", true);
        String buttonText = "View Details";
        if(!isActive) buttonText = "Vote";
       
        //Create the list of invites that the user can view. 
        eventList = (LinearLayout) findViewById(R.id.allinvites);
        initializeEventList(buttonText);
        
        Button done = (Button) findViewById(R.id.doneviewing);
        done.setOnClickListener(this);
	}

	private void initializeEventList(String buttonText)
	{
        Invitation[] myInvites = getInvitesFromIntent(this.getIntent());
        
        // Each row in the eventList will contain a LinearLayout from the array "events."
        //  That linearLayout will contain a button and the name of an event
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
        	ib.setInviteList(inviteData.getStringArray(INVITE_LIST));
        	ib.setSender(inviteData.getString(SENDER));
        	ib.setTimeout(inviteData.getFloat(TIMEOUT));
        	ib.setVoteData(inviteData.getStringArray(VOTE_DATA));
        		
        	inviteArray.add(Invitation.fromInvitationBean(ib));
        }

        Invitation[] myInvites = (Invitation[]) inviteArray.toArray();
        return myInvites;
	}

	public void onClick(View arg0) {
		Intent voteIntent = new Intent();
		
		if(!isActive)
		{
			
		}
		
		setResult(RESULT_OK, voteIntent);
		finish();
	}
}
