package edu.cmu.partytracer.model.invitation;

import android.os.Bundle;

import edu.cmu.partytracer.activity.invitation.CreationDialog;

public class BundleParser {

	public static String[] parseEventData(Bundle eventProps)
	{
		String eventName = eventProps.getString(CreationDialog.EVENT_NAME);
		String eventDesc = eventProps.getString(CreationDialog.EVENT_DETAILS);
		String[] eventData = new String[Invitation.NUM_DATA_ITEMS];
		eventData[Invitation.TITLE_INDEX] = eventName;
		eventData[Invitation.DESCRIPTION_INDEX] = eventDesc;
		
		return eventData;
	}
}
