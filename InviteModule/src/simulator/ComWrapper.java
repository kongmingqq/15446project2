package simulator;

public class ComWrapper {
	private static volatile AbstractComm communication;
	
	public static AbstractComm getComm()
	{
		if(communication == null)
			communication = new SimComm();
		return communication;
	}
}
