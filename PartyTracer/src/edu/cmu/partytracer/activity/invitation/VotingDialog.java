package edu.cmu.partytracer.activity.invitation;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.partytracer.R;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.model.invitation.OptionList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	private HashMap<String, ArrayList<String>> votes;
	private static String VOTE_TAG = "Voting Dialog";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voting);

        votes = new HashMap<String, ArrayList<String>>();
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
			votes.put(headers.get(i), new ArrayList<String>());
			
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
	
	private ArrayList<String> writeVotingArray()
	{
		ArrayList<String> votingArray = new ArrayList<String>();
		ArrayList<String> headers = new ArrayList<String>(votes.keySet());
		Log.d(VOTE_TAG, "Writing vote array");
		
		for(int i=0; i<headers.size(); i++)
		{
			votingArray.add(Invitation.categoryString);
			votingArray.add(headers.get(i));
			Log.d(VOTE_TAG, "Writing category " + headers.get(i));
			
			votingArray.addAll(votes.get(headers.get(i)));
		}
		
		votingArray.add(Invitation.endString);
		
		return votingArray;
	}
	
	public void onClick(View v) {
		if(!thisInvite.isActive())
		{
			Intent voteIntent = new Intent();
			
			Bundle voteBundle = new Bundle();
			voteBundle.putInt(ViewInvitesDialog.INVITE, thisInvite.getId());
			voteBundle.putStringArrayList(ViewInvitesDialog.VOTE_DATA, writeVotingArray());
			
			voteIntent.putExtra(ViewInvitesDialog.VOTING, voteBundle);
			setResult(RESULT_OK, voteIntent);
		}
		
		finish();
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String optionName = buttonView.getText().toString();
		String category = ((TextView)((LinearLayout)buttonView.getParent()).getChildAt(0)).getText().toString();
		
		if(isChecked)
		{
			Log.d(VOTE_TAG, "Adding vote for " + optionName + " to category " + category);
			votes.get(category).add(optionName);
		}
		else
		{
			Log.d(VOTE_TAG, "Removing vote for " + optionName + " from category " + category);
			votes.get(category).remove(optionName);
		}
	}
}
