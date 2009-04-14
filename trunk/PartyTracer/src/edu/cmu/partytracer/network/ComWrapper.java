package edu.cmu.partytracer.network;

public class ComWrapper {
	private static volatile Communicator communication;
	
	public static Communicator getComm()
	{
		if(communication == null)
			communication = new Communicator();
		return communication;
	}
}
