package edu.cmu.partytracer.activity.invitation;

import edu.cmu.partytracer.R;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.network.ComWrapper;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RequestDialog extends Activity implements View.OnClickListener{

	private EditText id;
	
	public void onCreate(Bundle savedInstanceState) {
    	    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);
        
        Button send = (Button) findViewById(R.id.sendrequest);
        id = (EditText) findViewById(R.id.inviteid);
        
        send.setOnClickListener(this);
    }

	public void onClick(View arg0) {
		String inviteId = id.getText().toString();
		Log.d("Request Dialog", "Sending request for invite " + inviteId);
		ComWrapper.getComm().send(Protocol.TYPE_Request, inviteId);
		finish();
	}
}
