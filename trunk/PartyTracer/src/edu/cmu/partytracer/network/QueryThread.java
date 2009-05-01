package edu.cmu.partytracer.network;

import android.util.Log;
import edu.cmu.partytracer.bean.Protocol;

public class QueryThread extends Thread {
	
	private String toQuery;
	
	public QueryThread(String inviteId)
	{
		ComWrapper.finishedQuerying = false;
		toQuery = inviteId;
	}
	
	public void run()
	{
		while(!ComWrapper.finishedQuerying)
		{
			Log.d("Query Thread", "Querying status of invitation " + toQuery);
			ComWrapper.getComm().send(Protocol.TYPE_ResultRequest, toQuery);
			ComWrapper.getComm().reset();
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				Log.d("Query Thread", "Query Thread was interrupted");
			}
		}
	}
}
