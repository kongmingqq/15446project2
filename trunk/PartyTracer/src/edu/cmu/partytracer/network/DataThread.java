package edu.cmu.partytracer.network;

import java.util.Vector;

import edu.cmu.partytracer.ptsocket.PTSocket;

public class DataThread extends Thread {

	private PTSocket inputSocket;
	private boolean crashed;
	
	public DataThread(PTSocket s)
	{
		inputSocket = s;
		crashed = false;
	}
	
	public boolean isCrashed() {
		return crashed;
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		crashed = false;
					
		try {
			Vector<Object> dataIn = new Vector<Object>();
			
			while((dataIn = (Vector<Object>) inputSocket.receiveObject()) != null)
			{
				MessageHandler.forward(dataIn);
			}
		} catch (Exception e) {
			crashed = true;
		}
		
	}
}
