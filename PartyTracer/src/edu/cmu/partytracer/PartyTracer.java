package edu.cmu.partytracer;

import edu.cmu.partytracer.activity.trace.*;
import edu.cmu.partytracer.activity.invitation.*;
import edu.cmu.partytracer.activity.invitation.testing.TestDataGenerator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PartyTracer extends Activity implements View.OnClickListener{
	EditText testServerIp;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button moduleTrace = (Button) findViewById(R.id.module_trace);
        Button moduleInvite = (Button) findViewById(R.id.module_invite);
        Button testTraceToggle = (Button) findViewById(R.id.test_tracetoggle);
        testServerIp = (EditText) findViewById(R.id.test_serverip);
        testServerIp.setText(Application.SERVER_IP);
        Button testSetServer = (Button) findViewById(R.id.test_setserver);
        
        moduleInvite.setOnClickListener(this);
        moduleTrace.setOnClickListener(this);
        testTraceToggle.setOnClickListener(this);
        testSetServer.setOnClickListener(this);
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
		else if(v.getId() == R.id.test_tracetoggle)
		{
			if(Application.CURRENT_PARTY_ID == null) {
				Application.CURRENT_PARTY_ID = "1000";
				Toast.makeText(getBaseContext(), "Party id set to 1000", Toast.LENGTH_SHORT).show();
			} else if(Application.CURRENT_PARTY_ID.equals("1000")) {
				Application.CURRENT_PARTY_ID = null;
				Toast.makeText(getBaseContext(), "Party id set to NULL", Toast.LENGTH_SHORT).show();
			}
			
		}
		else if(v.getId() == R.id.test_setserver)
		{
			String ip = testServerIp.getText().toString();
			if(ip != null && ip.length()!=0) {
				Application.SERVER_IP = ip;
				Toast.makeText(getBaseContext(), "Server IP set to \n"+ip, Toast.LENGTH_SHORT).show();
			} else {
				Application.SERVER_IP = null;
				Toast.makeText(getBaseContext(), "Server IP set to NULL", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
}