package edu.cmu.partytracer;

import edu.cmu.partytracer.activity.trace.*;
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
        
        moduleTrace.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.module_trace)
		{
			Intent i = new Intent(this, Map.class);
			startActivity(i);
		}
		
	}
}