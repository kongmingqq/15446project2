package edu.cmu.partytracer.controller;

import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.partytracer.dataProcessor.ClientCommunicator;
import edu.cmu.partytracer.serverThread.ServerSingleton;

public class VoteTimer {
	Timer timer;
	String partyID;

	public VoteTimer(long seconds, String partyID) {
		timer = new Timer();
		this.partyID = partyID;
		timer.schedule(new ToDoTask(), seconds * 1000);
	}

	class ToDoTask extends TimerTask {
		public void run() {
			if (!ServerSingleton.getInstance().getCurStatus(partyID).equals("VOTE_RESULT_SENT"))
				ClientCommunicator.sendVoteResult(partyID);
			timer.cancel(); // Terminate the thread
		}
	}
}