package edu.cmu.partytracer.network;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;

import android.util.Log;

public class DataThread extends Thread {

	private Socket inputSocket;
	
	public DataThread(Socket s)
	{
		inputSocket = s;
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{		
		try {
			Vector<Object> dataIn = new Vector<Object>();
			ObjectInputStream objStream = new ObjectInputStream(inputSocket.getInputStream());
			
			while((dataIn = (Vector<Object>) objStream.readObject()) != null)
			{
				Log.d("Data Thread", "Checking for incoming messages");
				Thread.sleep(1000);
				
				MessageHandler.forward(dataIn);
			}
			
			inputSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
