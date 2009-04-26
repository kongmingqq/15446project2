package edu.cmu.partytracer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Message extends Activity implements View.OnClickListener{
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        TextView text = (TextView) findViewById(R.id.message_text);
        
        text.setText(getIntent().getExtras().getString("Message"));
        Button okButton = (Button) findViewById(R.id.message_ok);
        
        okButton.setOnClickListener(this);
    }

	public void onClick(View v) {
		if(v.getId() == R.id.message_ok) {
			finish();
		}		
	}
}
