package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;
import java.util.Vector;

import edu.cmu.partytracer.R;

import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.network.ComWrapper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CreationDialog extends Activity implements View.OnClickListener{

	private ListView inviteUsers;
	private LinearLayout options;
	private Vector<String> votingData;
	
	public static String EVENT_NAME = "name";
	public static String EVENT_DESCRIPTION = "description";
	public static String EVENT_DETAILS = "details";
	public static String INVITED_LIST = "invited";
	public static String VOTING_DETAILS = "voting";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.creation);
		
		votingData = new Vector<String>();
		
		Button create = (Button) findViewById(R.id.createevent);
		Button cancel = (Button) findViewById(R.id.cancel);
		Button addCategory = (Button) findViewById(R.id.addcategory);
		
		create.setOnClickListener(this);
		cancel.setOnClickListener(this);
		addCategory.setOnClickListener(this);
		
		inviteUsers = (ListView) findViewById(R.id.invitable);
		ArrayAdapter<String> userList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ComWrapper.getComm().getPhoneBook());
		inviteUsers.setAdapter(userList);
		inviteUsers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		options = (LinearLayout) findViewById(R.id.alloptions);
	}

	private ArrayList<String> getInvitedUsers()
	{
		ListAdapter adapt = inviteUsers.getAdapter();
		int numItems = adapt.getCount();
		ArrayList<String> invited = new ArrayList<String>();
		
		for(int i=0; i<numItems; i++)
		{
			if(inviteUsers.isItemChecked(i))
				invited.add(adapt.getItem(i).toString());
		}
		
		return invited;
	}
	
	public void onClick(View v) {
		if(v.getId() == R.id.createevent)
		{
			String eventName = ((EditText)findViewById(R.id.eventname)).getText().toString();
			String eventData = ((EditText)findViewById(R.id.eventdescription)).getText().toString();
		
			Bundle eventDetails = new Bundle();
			eventDetails.putString(EVENT_NAME, eventName);
			eventDetails.putString(EVENT_DESCRIPTION, eventData);
		
			Intent eventProps = new Intent();
			eventProps.putExtra(EVENT_DETAILS, eventDetails);
			eventProps.putStringArrayListExtra(INVITED_LIST, getInvitedUsers());
			
			for(int i=0; i<options.getChildCount(); i++)
			{
				
			}
			eventProps.putStringArrayListExtra(Invitation.voterString, new ArrayList<String>(votingData));
			
			setResult(RESULT_OK, eventProps);
			finish();
		}
		else if(v.getId() == R.id.addcategory)
		{
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
			setResult(RESULT_CANCELED);
			finish();
		}
		else
		{
			LinearLayout optName = new LinearLayout(this);
			optName.setOrientation(LinearLayout.VERTICAL);
			TextView catLabel = new TextView(this);
			catLabel.setText("Option Name");
			EditText catName = new EditText(this);
			
			optName.addView(catLabel);
			optName.addView(catName);
			
			((ViewGroup) v.getParent()).addView(optName);
			v.getParent().requestLayout();
		}
	}
}
