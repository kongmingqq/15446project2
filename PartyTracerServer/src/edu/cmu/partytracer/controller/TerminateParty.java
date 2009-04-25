package edu.cmu.partytracer.controller;

import edu.cmu.partytracer.serverThread.ServerSingleton;

public class TerminateParty {
	public static void terminatePary(String partyID){
		ServerSingleton.getInstance().curStatus.remove(partyID);
		ServerSingleton.getInstance().voteMap.remove(partyID);
		ServerSingleton.getInstance().clientAddressMap.remove(partyID);
		ServerSingleton.getInstance().invitationMap.remove(partyID);
		ServerSingleton.getInstance().locationCacheMap.remove(partyID);
		ServerSingleton.getInstance().locationQueueMap.remove(partyID);
	}
}
