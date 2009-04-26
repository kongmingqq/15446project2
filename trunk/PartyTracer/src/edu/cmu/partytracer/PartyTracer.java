package edu.cmu.partytracer;

import edu.cmu.partytracer.activity.trace.*;
import edu.cmu.partytracer.activity.invitation.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PartyTracer extends Activity implements View.OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button moduleTrace = (Button) findViewById(R.id.module_trace);
        Button moduleInvite = (Button) findViewById(R.id.module_invite);
        
        moduleInvite.setOnClickListener(this);
        moduleTrace.setOnClickListener(this);
    }

	public void onClick(View v) {
		if(v.getId() == R.id.module_trace)
		{
	    	if(Application.CURRENT_PARTY_ID == null) {
				Intent i = new Intent(this, Message.class);
				i.putExtra("Message", "There is no party live now");
				startActivity(i);
				return;
	    	}
			Intent i = new Intent(this, Map.class);
			startActivity(i);
		}
		else if(v.getId() == R.id.module_invite)
		{
			Intent i = new Intent(this, InviteModule.class);
			startActivity(i);
		}
		
	}
}