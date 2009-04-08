package dialogs;

import java.util.ArrayList;

import project.invite.R;
import simulator.ComWrapper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CreationDialog extends Activity implements View.OnClickListener{

	private ListView inviteUsers;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.creation);
		
		Button create = (Button) findViewById(R.id.createevent);
		Button cancel = (Button) findViewById(R.id.cancel);
		
		create.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		inviteUsers = (ListView) findViewById(R.id.invitable);
		ArrayAdapter<String> userList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ComWrapper.getComm().getPhoneBook());
		inviteUsers.setAdapter(userList);
		inviteUsers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
			eventDetails.putString("name", eventName);
			eventDetails.putString("description", eventData);
		
			Intent eventProps = new Intent();
			eventProps.putExtra("details", eventDetails);
			eventProps.putStringArrayListExtra("invited", getInvitedUsers());
			
			setResult(RESULT_OK, eventProps);
			finish();
		}
		else
		{
			setResult(RESULT_CANCELED);
			finish();
		}
	}
}
