package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.partytracer.bean.InvitationBean;

import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	public static String OPTIONS = "options";
	
	public static String VOTING = "voting";
	
	private LinearLayout eventList;
	private boolean isActive;
	private Invitation[] myInvites;
	private HashMap<Integer, ArrayList<String>> voteResults;
	private static String VIEW_TAG = "View Invites Dialog";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewinvites);
        
        Log.d(VIEW_TAG, "Entering view dialog");
        voteResults = new HashMap<Integer, ArrayList<String>>();
        
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
        myInvites = getInvitesFromIntent(this.getIntent());
        
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
        	inviteButtons[i].setId(i);
        	inviteButtons[i].setOnClickListener(this);
        	
        	events[i].addView(inviteNames[i]);
        	events[i].addView(inviteButtons[i]);
        	eventList.addView(events[i]);
        	
        	voteResults.put(myInvites[i].getId(), new ArrayList<String>());
        }
	}
	
	public static Invitation[] getInvitesFromIntent(Intent iData)
	{
		ArrayList<Invitation> inviteArray = new ArrayList<Invitation>();
        int item = 0;
        
        while(iData.hasExtra(INVITE + item))
        {
            Log.d(VIEW_TAG, "Getting invite " + item);
        	Bundle inviteData = iData.getBundleExtra(INVITE + item);
        	InvitationBean ib = new InvitationBean();
        	
        	ib.setActive(inviteData.getBoolean(ACTIVE));
        	ib.setData(inviteData.getStringArray(DATA));
        	ib.setId(inviteData.getInt(IID));
        	ib.setInviteList(inviteData.getStringArray(INVITE_LIST));
        	ib.setSender(inviteData.getString(SENDER));
        	ib.setTimeout(inviteData.getFloat(TIMEOUT));
        	ib.setVoteData(inviteData.getStringArray(VOTE_DATA));
        	ib.setOptions(inviteData.getStringArray(OPTIONS));
        		
        	inviteArray.add(Invitation.fromInvitationBean(ib));
        	item++;
        }

        Invitation[] myInvites = new Invitation[inviteArray.size()];
        
        for(int i=0; i<myInvites.length; i++)
        {
        	myInvites[i] = inviteArray.get(i);
        }
        
        return myInvites;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Bundle voteBundle = data.getBundleExtra(VOTING);
		
		int invId = voteBundle.getInt(INVITE);
		ArrayList<String> votes = voteBundle.getStringArrayList(VOTE_DATA);
		
		voteResults.put(invId, votes);
	}
	
	public void onClick(View arg0) {
		if(arg0.getId() == R.id.doneviewing)
		{
			Intent voteIntent = new Intent();
			
			if(!isActive)
			{
				ArrayList<Integer> inviteIds = new ArrayList<Integer>(voteResults.keySet());
				
				for(int i=0; i<inviteIds.size(); i++)
				{
					Bundle voteBundle = new Bundle();
					voteBundle.putInt(INVITE, inviteIds.get(i));
					
					String[] voteArray = new String[voteResults.get(inviteIds.get(i)).size()];
					Log.d(VIEW_TAG, "Votes for invite " + inviteIds.get(i));
					
					for(int j=0; j<voteArray.length; j++)
					{
						voteArray[j] = voteResults.get(inviteIds.get(i)).get(j);
						Log.d(VIEW_TAG, voteArray[j]);
					}
					
					voteBundle.putStringArray(VOTE_DATA, voteArray);
					
					voteIntent.putExtra(VOTING + i, voteBundle);
				}
			}
			
			setResult(RESULT_OK, voteIntent);
			finish();
		}
		else
		{
			Bundle invBundle = InviteModule.constructInviteBundle(myInvites[arg0.getId()].toInvitationBean());
			Intent voteIntent = new Intent(this, edu.cmu.partytracer.activity.invitation.VotingDialog.class);
			voteIntent.putExtra(INVITE + 0, invBundle);
			
			if(!isActive)
				startActivityForResult(voteIntent, 0);
			else
				startActivity(voteIntent);
		}
	}
}
