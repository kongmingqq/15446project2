package edu.cmu.partytracer.network;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;

import android.util.Log;

public class DataThread extends Thread {

	private Socket inputSocket;
	private boolean crashed;
	
	public DataThread(Socket s)
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
			ObjectInputStream objStream = new ObjectInputStream(inputSocket.getInputStream());
			
			while(true)
			{
				Log.d("Data Thread", "Checking for incoming messages");
				Thread.sleep(1000);
				
				dataIn = (Vector<Object>) objStream.readObject();
				if(dataIn != null)
					MessageHandler.forward(dataIn);
			}
		} catch (Exception e) {
			crashed = true;
		}
		
	}
}
