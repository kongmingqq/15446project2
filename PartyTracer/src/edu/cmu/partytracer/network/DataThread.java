package edu.cmu.partytracer.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import android.util.Log;

public class DataThread extends Thread {

	private ServerSocket inputSocket;
	
	public DataThread(int port)
	{
		try {
			inputSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{		
		try {
			Vector<Object> dataIn = new Vector<Object>();
			Log.d("Data Thread", "Entering run function");
			
			while(true)
			{
				Log.d("Data Thread", "Checking for incoming messages");
				Socket s = inputSocket.accept();
				ObjectInputStream objStream = new ObjectInputStream(s.getInputStream());
				Object next = objStream.readObject();
				
				if(next instanceof Vector)
				{
					dataIn = (Vector<Object>) objStream.readObject();
					Log.d("Data Thread", "Received a message");
					MessageHandler.forward(dataIn);
				}
				else
				{
					Log.d("Data Thread", "Received a non-vector message");
					Log.d("Data Thread", next.toString());
				}
				Thread.sleep(1000);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
