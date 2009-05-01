package edu.cmu.partytracer.controller;

import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.partytracer.dataProcessor.ClientCommunicator;
import edu.cmu.partytracer.serverThread.ServerSingleton;

/**
 * Timer for the voting stage
 * @author Xiaojian Huang
 *
 */
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
//				ClientCommunicator.sendVoteResult(partyID);
				ServerSingleton.getInstance().setCurStatus(partyID, "RESULT_SEND_TIMEOUT");
			timer.cancel(); // Terminate the thread
		}
	}
}
