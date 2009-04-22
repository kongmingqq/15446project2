package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.partytracer.R;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.model.invitation.OptionList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton;

public class VotingDialog extends Activity implements View.OnClickListener,
														CompoundButton.OnCheckedChangeListener{

	private Invitation thisInvite;
	private LinearLayout categories;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voting);
        
        Invitation[] inviteList = ViewInvitesDialog.getInvitesFromIntent(this.getIntent());
        thisInvite = inviteList[0];
        
        Button done = (Button) findViewById(R.id.donevoting);
        done.setOnClickListener(this);
        
        categories = (LinearLayout) findViewById(R.id.allvotingoptions);
        constructOptionList();
	}

	private void constructOptionList()
	{
		HashMap<String, OptionList> optList = thisInvite.getOptions();
		ArrayList<String> headers = new ArrayList<String>(optList.keySet());
		
		for(int i=0; i<headers.size(); i++)
		{
			LinearLayout singleCategory = new LinearLayout(this);
			String[] voteOptions = optList.get(headers.get(i)).getAllOptions();
			
			TextView catName = new TextView(this);
			catName.setText(headers.get(i));
			singleCategory.addView(catName);
			
			if(!thisInvite.isActive())
			{
				for(int j=0; j<voteOptions.length; j++)
				{
					CheckBox checkOption = new CheckBox(this);
					checkOption.setText(voteOptions[j]);
					checkOption.setChecked(false);
					checkOption.setOnCheckedChangeListener(this);
					singleCategory.addView(checkOption);
				}
			}
			else
			{
				TextView fixedOption = new TextView(this);
				fixedOption.setText(voteOptions[0]);
				singleCategory.addView(fixedOption);
			}
			
			categories.addView(singleCategory);
		}
	}
	
	public void onClick(View v) {
		
		
		finish();
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String optionName = buttonView.getText().toString();
		
	}
}
