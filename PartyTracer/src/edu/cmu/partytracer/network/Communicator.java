package edu.cmu.partytracer.network;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import edu.cmu.partytracer.Application;

import android.util.Log;

public class Communicator extends AbstractComm{
	private Socket outputSocket;
	private String myNumber;
	
	public Communicator() {
		try
		{
			Log.d("Communicator", "Creating TCP Socket");
			Log.d("Communicator", "Finished creating TCP Socket");
			DataThread serverListener = new DataThread(5000);
			serverListener.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMyNumber() {
		return myNumber;
	}

	public boolean send(String identifier, Object obj)
	{
		Log.d("Communicator", "Entering send function");
		try 
		{
			Log.d("Communicator", "Entering try block");
			Vector<Object> data = new Vector<Object>();
			data.add(identifier);
			data.add(obj);

			Log.d("Communicator", "Opening socket");
			outputSocket = new Socket(Application.SERVER_IP, 15446);
			Log.d("Communicator", "Opened");
			ObjectOutputStream objStream = new ObjectOutputStream(outputSocket.getOutputStream());
			objStream.writeObject(data);
			Log.d("Communicator", "Wrote an object");
			objStream.flush();
			Log.d("Communicator", "Closing socket");
			outputSocket.close();
			Log.d("Communicator", "Closed socket");
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void initNumber(String myNumber) {
		this.myNumber = myNumber;
	}
}
