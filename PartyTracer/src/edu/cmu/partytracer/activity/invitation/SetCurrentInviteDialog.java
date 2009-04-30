package edu.cmu.partytracer.activity.invitation;

import edu.cmu.partytracer.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.util.Log;
import android.view.View;

public class SetCurrentInviteDialog extends Activity implements View.OnClickListener, OnCheckedChangeListener{
	private String selected;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setcurrent);
        selected = "";
        
        Button done = (Button) findViewById(R.id.donesetting);
        done.setOnClickListener(this);
        
        RadioGroup inviteList = (RadioGroup) findViewById(R.id.allactiveinvites);
        inviteList.setOnCheckedChangeListener(this);
        getEventNames(this.getIntent(), inviteList);
	}

	private void getEventNames(Intent iData, RadioGroup list)
	{
		int item = 0;
		
		while(iData.hasExtra(ViewInvitesDialog.INVITE + item))
		{
			Log.d("Set Current", "Listing invite number " + item);
			RadioButton nextInvite = new RadioButton(this);
			nextInvite.setText(iData.getStringExtra(ViewInvitesDialog.INVITE + item));
			list.addView(nextInvite);
			
			item++;
		}
	}
	
	public void onClick(View v) {
		Intent setIntent = new Intent();
		
		setIntent.putExtra(ViewInvitesDialog.IID, selected);

		setResult(RESULT_OK, setIntent);
		finish();
	}

	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		RadioButton invite = (RadioButton)findViewById(arg1);
		selected = invite.getText().toString();
	}
}
