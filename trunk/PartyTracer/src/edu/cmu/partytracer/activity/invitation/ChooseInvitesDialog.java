package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;

import edu.cmu.partytracer.R;
import edu.cmu.partytracer.activity.invitation.testing.TestDataGenerator;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ChooseInvitesDialog extends Activity implements View.OnClickListener, OnCheckedChangeListener{
	public static String INVITED_DATA = "invited users";
	
	private ArrayList<String> invitedUserNumbers;
	private static String CHOOSE_INVITES_TAG = "Choose Invited Users";
	private Cursor contacts;
	private LinearLayout contactList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inviteusers);
		
		Log.d(CHOOSE_INVITES_TAG, "Entered Choose Invites Dialog");
		
		contactList = (LinearLayout) findViewById(R.id.allinvitableusers);
		if(TestDataGenerator.TEST_MODE_ON)
		{
			String[] contacts = TestDataGenerator.samplePhoneBook();
			
			for(int i=0; i<contacts.length; i++)
			{
				CheckBox entry = new CheckBox(this);
				entry.setText(contacts[i]);
				entry.setOnCheckedChangeListener(this);
				contactList.addView(entry);
			}
		}
		else
		{
			String[] cols = new String[] {People.NAME, People.NUMBER};
			contacts = getContentResolver().query(People.CONTENT_URI, cols, null, null, People.NAME + " ASC");
		}
		
		invitedUserNumbers = new ArrayList<String>();
		
		Log.d(CHOOSE_INVITES_TAG, "Ready to create done button");
		Button done = (Button) findViewById(R.id.finishedinviting);
		done.setOnClickListener(this);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		String phoneNum = "";
		String selectedName = ((TextView) v).getText().toString();
		
		if(TestDataGenerator.TEST_MODE_ON)
		{
			phoneNum = TestDataGenerator.lookUpContact(selectedName);
		}
		else
		{
			contacts.moveToFirst();
			int nameColumn = contacts.getColumnIndex(People.NAME);
			int numColumn = contacts.getColumnIndex(People.PRIMARY_PHONE_ID);
			
			do
			{
				String name = contacts.getString(nameColumn);
				if(name.equals(selectedName))
				{
					phoneNum = contacts.getString(numColumn);
					break;
				}
			} while(contacts.moveToNext());
		}
		
		Log.d(CHOOSE_INVITES_TAG, "Selected user " + phoneNum);
		
		if(invitedUserNumbers.contains(phoneNum))
			invitedUserNumbers.remove(phoneNum);
		else
			invitedUserNumbers.add(phoneNum);
	}

	public void onClick(View v) {
		Intent invIntent = new Intent();
		Log.d(CHOOSE_INVITES_TAG, "Exiting Choose Invites Dialog");
		
		for(int i=0; i<invitedUserNumbers.size(); i++)
		{
			Log.d(CHOOSE_INVITES_TAG, "Invited user " + invitedUserNumbers.get(i));
		}
		
		invIntent.putStringArrayListExtra(INVITED_DATA, invitedUserNumbers);
		
		setResult(RESULT_OK, invIntent);
		finish();
	}

	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		String phoneNum;
		
		if(TestDataGenerator.TEST_MODE_ON)
		{
			phoneNum = TestDataGenerator.lookUpContact(arg0.getText().toString());
		}
		else
		{
			phoneNum = TestDataGenerator.lookUpContact(arg0.getText().toString());
		}
		
		Log.d(CHOOSE_INVITES_TAG, "Selected user " + phoneNum);
		
		if(arg1)
		{
			if(!invitedUserNumbers.contains(phoneNum))
			{
				invitedUserNumbers.add(phoneNum);
				Log.d(CHOOSE_INVITES_TAG, "Invited user " + phoneNum);
			}
		}
		else
		{
			invitedUserNumbers.remove(phoneNum);
			Log.d(CHOOSE_INVITES_TAG, "Removed user " + phoneNum + " from invite list");
		}
	}
}
