package edu.cmu.partytracer.model.invitation;

import android.os.Bundle;
import android.util.Log;

import edu.cmu.partytracer.activity.invitation.CreationDialog;

public class BundleParser {

	private static String PARSE_TAG = "Bundle Parser";
	
	public static String[] parseEventData(Bundle eventProps)
	{
		Log.d(PARSE_TAG, "Extracting event data");
		
		String eventName = eventProps.getString(CreationDialog.EVENT_NAME);
		String eventDesc = eventProps.getString(CreationDialog.EVENT_DESCRIPTION);
		
		Log.d(PARSE_TAG, eventName);
		Log.d(PARSE_TAG, eventDesc);
		
		String[] eventData = new String[Invitation.NUM_DATA_ITEMS];
		eventData[Invitation.TITLE_INDEX] = eventName;
		eventData[Invitation.DESCRIPTION_INDEX] = eventDesc;
		
		return eventData;
	}
}
