package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;

import edu.cmu.partytracer.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ChooseInvitesDialog extends ListActivity implements View.OnClickListener{
	public static String INVITED_DATA = "invited users";
	
	private ListAdapter mAdapter;
	private ArrayList<String> invitedUserNumbers;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Cursor contacts = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
		startManagingCursor(contacts);

		String[] columns = new String[] {People.NAME};
		int[] names = new int[] {R.id.row_entry};

		mAdapter = new SimpleCursorAdapter(this, R.layout.inviteusers, contacts, columns, names);
		setListAdapter(mAdapter);
		
		invitedUserNumbers = new ArrayList<String>();
		
		Button done = (Button) findViewById(R.id.finishedinviting);
		done.setOnClickListener(this);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor contactInfo = (Cursor) mAdapter.getItem(position);
		String phoneNum = contactInfo.getString(contactInfo.getColumnIndexOrThrow(People.PRIMARY_PHONE_ID));
		
		if(invitedUserNumbers.contains(phoneNum))
			invitedUserNumbers.remove(phoneNum);
		else
			invitedUserNumbers.add(phoneNum);
	}

	public void onClick(View v) {
		Intent invIntent = new Intent();
		invIntent.putStringArrayListExtra(INVITED_DATA, invitedUserNumbers);
		
		setResult(RESULT_OK, invIntent);
		finish();
	}
}
