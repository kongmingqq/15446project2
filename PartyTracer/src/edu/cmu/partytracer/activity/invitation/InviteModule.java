package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cmu.partytracer.activity.invitation.testing.TestDataGenerator;
import edu.cmu.partytracer.activity.trace.TraceSendThread;
import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.network.AlertThread;
import edu.cmu.partytracer.network.ComWrapper;
import edu.cmu.partytracer.Application;
import edu.cmu.partytracer.R;
import edu.cmu.partytracer.model.invitation.BundleParser;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.model.invitation.UserSingleton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InviteModule extends Activity implements View.OnClickListener{
	
	static int CREATE_INVITE = 1;
	static int VIEW_VOTES = 2;
	
	private static String MAIN_TAG = "Invite Module";
	private LinearLayout alertList;
	private static int DEFAULT_TIMEOUT = 120;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {   	    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite);

        Log.d(MAIN_TAG, "Connecting to server");
    	ComWrapper.initInstance();
    	Log.d(MAIN_TAG, "Success");
    	initPhoneNumber();    	//Get this user's phone number and store it in the singleton class so that other methods can access it

    	Button createInvite = (Button) findViewById(R.id.create);
        Button viewActive = (Button) findViewById(R.id.active);
        Button viewVotes = (Button) findViewById(R.id.vote);
        Button request = (Button) findViewById(R.id.request);
        Button exit = (Button) findViewById(R.id.exit);
        
        createInvite.setOnClickListener(this);
        viewActive.setOnClickListener(this);
        viewVotes.setOnClickListener(this);
        request.setOnClickListener(this);
        exit.setOnClickListener(this);
        
        alertList = (LinearLayout) findViewById(R.id.allalerts);
        //AlertThread at = new AlertThread(this);
        //at.start();
    }
    
    public void checkAlerts()
    {
    	ArrayList<String> allAlerts = ComWrapper.getComm().getAlerts();
    	for(int i=0; i<allAlerts.size(); i++)
    	{
    		TextView alert = new TextView(this);
    		alert.setText(allAlerts.get(i));
    		alertList.addView(alert);
    	}
    	
    	if(allAlerts.size() > 0)
    		alertList.requestLayout();
    }
    
    private void initPhoneNumber()
    {
    	Log.d(MAIN_TAG, "Getting and storing user phone number");
    	String myNumber;
    	
    	if(TestDataGenerator.TEST_MODE_ON)
    	{
    		myNumber = TestDataGenerator.sampleID();
    		ComWrapper.getComm().initNumber(myNumber);
    	}
    	else
    	{
    		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    		myNumber = tm.getLine1Number();
    		ComWrapper.getComm().initNumber(myNumber);
    	}
    	
    	Log.d(MAIN_TAG, "This user's number is " + myNumber);
    }
    
	public void onClick(View v) {
		//There are three buttons that the user can click from this screen, and each one launches a new activity
		
		if(v.getId() == R.id.create)
		{
			//The first one is simple to launch: create a new invitation
			Intent create = new Intent(this, edu.cmu.partytracer.activity.invitation.CreationDialog.class);
			
			Log.d(MAIN_TAG, "Entering Create Invite module");
			startActivityForResult(create, CREATE_INVITE);
		}
		else if(v.getId() == R.id.request)
		{
			Intent subscribe = new Intent(this, edu.cmu.partytracer.activity.invitation.RequestDialog.class);
			startActivity(subscribe);
		}
		else if(v.getId() == R.id.exit)
		{
			//Start a trace thread here
			Application.TRACE_SEND_THREAD = new TraceSendThread();
			Application.MY_PHONE_ID = UserSingleton.getUser().getNumber();
			Invitation[] myInvites = UserSingleton.getUser().getMyInvites();
			
			for(int i=0; i<myInvites.length; i++)
			{
				if(myInvites[i].isActive())
				{
					Application.CURRENT_PARTY_ID = Integer.toString(myInvites[i].getId());
					break;
				}
			}
			
			Application.TRACE_SEND_THREAD.start();
			finish();
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
			Invitation[] myInvites = UserSingleton.getUser().getMyInvites();
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
			Log.d(MAIN_TAG, "View Voting invites clicked");
			
			//Get a list of each of this user's voting invites, and store each one in the intent used to
			// launch the new activity 
			Invitation[] myInvites = UserSingleton.getUser().getMyInvites();
			int item = 0;
			for(int i=0; i<myInvites.length; i++)
			{
				if(!myInvites[i].isActive())
				{
					Log.d(MAIN_TAG, "Putting item number " + item);
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
		invBundle.putInt(ViewInvitesDialog.IID, Integer.valueOf(ib.getId()));
		invBundle.putStringArray(ViewInvitesDialog.INVITE_LIST, ib.getInviteList());
		invBundle.putString(ViewInvitesDialog.SENDER, ib.getSender());
		invBundle.putFloat(ViewInvitesDialog.TIMEOUT, ib.getTimeout());
		invBundle.putStringArray(ViewInvitesDialog.VOTE_DATA, ib.getVoteData());
		invBundle.putStringArray(ViewInvitesDialog.OPTIONS, ib.getOptions());
		
		return invBundle;
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
        	if (requestCode == CREATE_INVITE) 
        	{
        		Log.d(MAIN_TAG, "Returned from creation module");
        		
        		//The user just created a new invitation, so take the information from the CreateInvites
        		// activity and create an invitation object from it
        		InvitationBean ib = new InvitationBean();
        		
        		//First, create the bean that will be sent to the server
        		Bundle eventProps = data.getBundleExtra(CreationDialog.EVENT_DETAILS);
        		String[] voteData = new String[0];
        		String[] eventData = BundleParser.parseEventData(eventProps);
        		long timeout = eventProps.getLong(CreationDialog.TIMEOUT, DEFAULT_TIMEOUT);
        		
        		Log.d(MAIN_TAG, "Event Properties:");
        		for(int i=0; i<eventData.length; i++)
        		{
        			Log.d(MAIN_TAG, eventData[i]);
        		}
        		
        		//fill in all the data fields for the invitation bean
        		ib.setSender(UserSingleton.getUser().getNumber());
        		ib.setVoteData(voteData);
        		ib.setData(eventData);
        		ib.setId("0");
        		
        		//Create and set the list of users who are invited to this event
        		ArrayList<String> invited = data.getStringArrayListExtra(CreationDialog.INVITED_LIST);
        		String[] invitedNumbers = new String[invited.size()];
        		
        		for(int i=0; i<invited.size(); i++)
        		{
        			Log.d(MAIN_TAG, "User " + invited.get(i) + " was invited");
        			invitedNumbers[i] = invited.get(i);
        		}
        		
        		ib.setInviteList(invitedNumbers);
        		
        		//Finally, get the array of strings which represents the available voting options
        		ArrayList<String> voteOptions = data.getStringArrayListExtra(Invitation.voterString);
        		String[] vOpts = new String[voteOptions.size()];
        		for(int i=0; i<voteOptions.size(); i++)
        		{
        			vOpts[i] = voteOptions.get(i);
        		}
        		ib.setOptions(vOpts);
        		ib.setTimeout(timeout);
        		
        		//add the invitation to our user's database and send it off to the server
        		//UserSingleton.getUser().addInvite(Invitation.fromInvitationBean(ib));
        		Log.d(MAIN_TAG, "About to send the invitation");
        		ComWrapper.getComm().send(Protocol.TYPE_InvitationBean, ib);
        	}
	        else if(requestCode == VIEW_VOTES)
	        {
	        	//The user just voted on something
	        	int item = 0;
	        	String[] singleVoter = new String[1];
	        	singleVoter[0] = UserSingleton.getUser().getNumber();
	        	
	        	while(data.hasExtra(ViewInvitesDialog.VOTING + item))
	        	{
		        	Bundle voteProps = data.getBundleExtra(ViewInvitesDialog.VOTING + item);
		        	VoteBean vb = new VoteBean();
		        	
		        	//Extract the information about what the user voted on and send it off
		        	ArrayList<String> voteData = new ArrayList<String>();
		        	voteData.add(Invitation.voterString + UserSingleton.getUser().getNumber());
		        	voteData.addAll(Arrays.asList(voteProps.getStringArray(ViewInvitesDialog.VOTE_DATA)));
		        	
		        	String[] votingArray = new String[voteData.size()];
		        	
		        	Log.d(MAIN_TAG, "The vote array that we got back");
		        	for(int i=0; i<votingArray.length; i++)
		        	{
		        		votingArray[i] = voteData.get(i);
		        		Log.d(MAIN_TAG, votingArray[i]);
		        	}
		        	
		        	vb.setData(votingArray);
		        	vb.setVoters(singleVoter);
		        	vb.setPartyId(Integer.toString(voteProps.getInt(ViewInvitesDialog.INVITE)));
		        	
		        	ComWrapper.getComm().send(Protocol.TYPE_VoteBean, vb);
		        	item++;
	        	}
	        }
        }
    }

}
