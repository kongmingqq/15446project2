package edu.cmu.partytracer.model.invitation;

import java.util.ArrayList;

import android.os.Bundle;

import edu.cmu.partytracer.activity.invitation.CreationDialog;
import edu.cmu.partytracer.network.ComWrapper;

public class BundleParser {
	public static int[] parseInvitedNumbers(ArrayList<String> invited)
	{
		int[] invitedNumbers = new int[invited.size()];
		
		for(int i=0; i<invitedNumbers.length; i++)
		{
			invitedNumbers[i] = ComWrapper.getComm().lookUp(invited.get(i));
		}
		
		return invitedNumbers;
	}
	
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
