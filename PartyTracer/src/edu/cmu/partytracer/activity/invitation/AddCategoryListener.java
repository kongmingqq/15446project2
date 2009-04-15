package edu.cmu.partytracer.activity.invitation;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddCategoryListener implements View.OnClickListener{

	private LinearLayout optionDetails;
	private Context myContext;
	
	public AddCategoryListener(LinearLayout newOption, Context c)
	{
		optionDetails = newOption;
		myContext = c;
	}
	
	public void onClick(View v) {
		LinearLayout optName = new LinearLayout(myContext);
		optName.setOrientation(LinearLayout.VERTICAL);
		TextView catLabel = new TextView(myContext);
		catLabel.setText("Option Name");
		EditText catName = new EditText(myContext);
		
		optName.addView(catLabel);
		optName.addView(catName);
		
		optionDetails.addView(optName);
		optionDetails.requestLayout();
	}

}
