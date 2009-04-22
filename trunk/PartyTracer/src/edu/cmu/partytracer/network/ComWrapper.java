package edu.cmu.partytracer.network;

public class ComWrapper {
	private static volatile AbstractComm communication;
	
	public static AbstractComm getComm()
	{
		if(communication == null)
			communication = new DummyCommunicator();
		return communication;
	}
}
