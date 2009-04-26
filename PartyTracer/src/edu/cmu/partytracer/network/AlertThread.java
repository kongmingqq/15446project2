package edu.cmu.partytracer.network;

import edu.cmu.partytracer.activity.invitation.InviteModule;

public class AlertThread extends Thread {

	private InviteModule app;
	
	public AlertThread(InviteModule mod)
	{
		app = mod;
	}
	
	public void run()
	{
		while(true)
		{
			app.checkAlerts();
		}
	}
}
