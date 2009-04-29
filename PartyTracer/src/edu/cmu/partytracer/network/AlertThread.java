package edu.cmu.partytracer.network;

import java.util.ArrayList;

import edu.cmu.partytracer.activity.invitation.InviteModule;

public class AlertThread extends Thread {

	private InviteModule app;
	
	public AlertThread(InviteModule mod)
	{
		app = mod;
	}
	
	public void run()
	{
		ArrayList<String> allAlerts = new ArrayList<String>();
		
		while(true)
		{
			allAlerts = ComWrapper.getComm().getAlerts();
			
			if(allAlerts.size() > 0)
				app.checkAlerts();
		}
	}
}
