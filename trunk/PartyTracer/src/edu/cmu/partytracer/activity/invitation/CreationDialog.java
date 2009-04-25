package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.cmu.partytracer.R;
import edu.cmu.partytracer.model.invitation.Invitation;

public class CreationDialog extends Activity implements View.OnClickListener{

	private LinearLayout options;
	private Vector<String> votingData;
	private ArrayList<String> invitedUsers;
	private static String CREATE_TAG = "Create Invites Screen";
	
	public static String EVENT_NAME = "name";
	public static String EVENT_DESCRIPTION = "description";
	public static String EVENT_DETAILS = "details";
	public static String INVITED_LIST = "invited";
	public static String VOTING_DETAILS = "voting";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.creation);
		
		invitedUsers = new ArrayList<String>();
		Log.d(CREATE_TAG, "Entered Creation Dialog");
		
		votingData = new Vector<String>();
		
		Button create = (Button) findViewById(R.id.createevent);
		Button cancel = (Button) findViewById(R.id.cancel);
		Button addCategory = (Button) findViewById(R.id.addcategory);
		Button addInvites = (Button) findViewById(R.id.addinvitedusers);
		
		create.setOnClickListener(this);
		cancel.setOnClickListener(this);
		addCategory.setOnClickListener(this);
		addInvites.setOnClickListener(this);
		
		options = (LinearLayout) findViewById(R.id.alloptions);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		invitedUsers = data.getStringArrayListExtra(ChooseInvitesDialog.INVITED_DATA);
		Log.d(CREATE_TAG, "Returned from Choose Invites Dialog");
		
		for(int i=0; i<invitedUsers.size(); i++)
		{
			Log.d(CREATE_TAG, "User " + invitedUsers.get(i) + " is invited");
		}
		
	}
	
	private void addVotingData()
	{
		Log.d(CREATE_TAG, "Creating voting array");
		
		for(int i=0; i<options.getChildCount(); i++)
		{
			LinearLayout newOption = (LinearLayout) options.getChildAt(i);
			LinearLayout catDetails = (LinearLayout) newOption.getChildAt(0);
			EditText catName = (EditText) catDetails.getChildAt(1);

			votingData.add(Invitation.categoryString);
			votingData.add(catName.getText().toString());
			Log.d(CREATE_TAG, "Adding category " + catName.getText().toString());
			
			for(int j=1; j<newOption.getChildCount() - 1; j++)
			{
				LinearLayout optName = (LinearLayout) newOption.getChildAt(j);
				EditText optValue = (EditText) optName.getChildAt(1);
				votingData.add(optValue.getText().toString());
				
				Log.d(CREATE_TAG, "Adding option " + optValue.getText().toString());
			}
		}
		
		votingData.add(Invitation.endString);
	}
	
	public void onClick(View v) {
		if(v.getId() == R.id.createevent)
		{
			Log.d(CREATE_TAG, "Creating invitation");
			
			String eventName = ((EditText)findViewById(R.id.eventname)).getText().toString();
			String eventData = ((EditText)findViewById(R.id.eventdescription)).getText().toString();
		
			Log.d(CREATE_TAG, "Event name is " + eventName);
			Log.d(CREATE_TAG, "Event description is " + eventData);
			
			Bundle eventDetails = new Bundle();
			eventDetails.putString(EVENT_NAME, eventName);
			eventDetails.putString(EVENT_DESCRIPTION, eventData);
		
			Intent eventProps = new Intent();
			eventProps.putExtra(EVENT_DETAILS, eventDetails);
			eventProps.putStringArrayListExtra(INVITED_LIST, invitedUsers);
			
			for(int i=0; i<invitedUsers.size(); i++)
			{
				Log.d(CREATE_TAG, "User " + invitedUsers.get(i) + " is invited");
			}
			
			addVotingData();
			eventProps.putStringArrayListExtra(Invitation.voterString, new ArrayList<String>(votingData));
			
			setResult(RESULT_OK, eventProps);
			finish();
		}
		else if(v.getId() == R.id.addcategory)
		{
			Log.d(CREATE_TAG, "Adding a new category to vote on");
			LinearLayout newOption = new LinearLayout(this);
			
			LinearLayout catDetails = new LinearLayout(this);
			catDetails.setOrientation(LinearLayout.VERTICAL);
			
			TextView catLabel = new TextView(this);
			catLabel.setText("Category Name");
			EditText catName = new EditText(this);
			
			catDetails.addView(catLabel);
			catDetails.addView(catName);
			
			Button addOptionButton = new Button(this);
			addOptionButton.setText("Add Option");
			addOptionButton.setOnClickListener(this);
			
			newOption.addView(catDetails);
			newOption.addView(addOptionButton);
			
			options.addView(newOption);
			options.requestLayout();
		}
		else if(v.getId() == R.id.cancel)
		{
			Log.d(CREATE_TAG, "Cancelling invite");
			setResult(RESULT_CANCELED);
			finish();
		}
		else if(v.getId() == R.id.addinvitedusers)
		{
			Log.d(CREATE_TAG, "Going to Choose Invited Users screen");
			Intent selectInvited = new Intent(this, edu.cmu.partytracer.activity.invitation.ChooseInvitesDialog.class);
			startActivityForResult(selectInvited, 0);
		}
		else
		{
			Log.d(CREATE_TAG, "Add Option clicked");
			String whatCategory = ((TextView) ((ViewGroup) (((ViewGroup) v.getParent()).getChildAt(0))).getChildAt(1)).getText().toString();
			Log.d(CREATE_TAG, "Adding a new option to category " + whatCategory);
			
			LinearLayout optName = new LinearLayout(this);
			optName.setOrientation(LinearLayout.VERTICAL);
			TextView catLabel = new TextView(this);
			catLabel.setText("Option Name");
			EditText catName = new EditText(this);
			
			optName.addView(catLabel);
			optName.addView(catName);
			int index = ((LinearLayout) v.getParent()).getChildCount() - 1;
			
			((ViewGroup) v.getParent()).addView(optName, index);
			v.getParent().requestLayout();
		}
	}
}
