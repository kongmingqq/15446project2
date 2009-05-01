package edu.cmu.partytracer.network;

import java.util.ArrayList;

public abstract class AbstractComm {
	private ArrayList<String> alertList;
	
	public abstract String getMyNumber();
	public abstract void send(String identifier, Object obj);
	public abstract void initNumber(String myNumber);
	public abstract void reset();
	public abstract void startQueryThread(String inviteId);
	public abstract void stopQueryThread(String inviteId);
	
	public void addAlert(String message)
	{
		if(alertList == null)
			alertList = new ArrayList<String>();
		else
			alertList.add(message);
	}
	public ArrayList<String> getAlerts()
	{
		if(alertList == null)
			return new ArrayList<String>();
		else
		{
			ArrayList<String> listCopy = new ArrayList<String>(alertList);
			alertList = new ArrayList<String>();
			return listCopy;
		}
	}
}
