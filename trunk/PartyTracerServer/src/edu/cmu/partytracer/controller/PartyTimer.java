package edu.cmu.partytracer.controller;

import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.partytracer.serverThread.ServerSingleton;

/**
 * Timer for terminate the party
 * @author Xiaojian Huang
 *
 */
public class PartyTimer {
	Timer timer;
	String partyID;
	public PartyTimer(long seconds, String partyID) {
		timer = new Timer();
		this.partyID = partyID;
		timer.schedule(new ToDoTask(), seconds * 1000);
	}

	class ToDoTask extends TimerTask {
		public void run() {
			TerminateParty.terminateParty(partyID);
			timer.cancel(); // Terminate the thread
		}
	}
}
