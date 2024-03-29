package edu.cmu.partytracer.network;

import edu.cmu.partytracer.activity.invitation.testing.DummyCommunicator;
import edu.cmu.partytracer.activity.invitation.testing.TestDataGenerator;

public class ComWrapper {
	private static volatile AbstractComm communication;
	public static volatile boolean finishedQuerying;
	
	public static void initInstance()
	{
		if(communication == null)
		{
			if(TestDataGenerator.TEST_MODE_ON)
				communication = new DummyCommunicator();
			else
				communication = new Communicator();
		}
	}
	public static AbstractComm getComm()
	{
		if(communication == null)
		{
			initInstance();
		}
		return communication;
	}
}
