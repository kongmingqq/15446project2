package edu.cmu.partytracer.network;

import java.io.IOException;
import java.util.Vector;

import edu.cmu.partytracer.Application;
import edu.cmu.partytracer.ptsocket.TCPSocket;

import android.util.Log;

public class Communicator extends AbstractComm{
	private TCPSocket commSocket;
	private String myNumber;
	private Vector<Object> unsentData;
	private boolean socketOpen;
	private DataThread serverListener;
	
	public Communicator() {
		socketOpen = false;
		unsentData = new Vector<Object>();	
		reset();
	}
	
	public String getMyNumber() {
		return myNumber;
	}

	private void reset()
	{
		try
		{
			Log.d("Communicator", "Creating TCP Socket");
			commSocket = new TCPSocket(Application.SERVER_IP, 15446);
			Log.d("Communicator", "Finished creating TCP Socket");

			serverListener = new DataThread(commSocket);
			serverListener.start();

			socketOpen = true;
		} catch (Exception e) {
			Log.d("Communicator", "Socket couldn't be set up");
			e.printStackTrace();
		}
		
		while(unsentData.size() > 0)
		{
			Object obj = unsentData.get(0);
			boolean sent = sendObject(obj);
			
			if(sent)
				unsentData.remove(0);
			else
				break;
		}
	}
	
	private void closeCommSocket()
	{
		try {
			commSocket.close();
			serverListener.shouldRun = false;
		} catch (IOException e) {

		}
	}
	
	private boolean sendObject(Object data)
	{
		try {
			Log.d("Communicator", "Sending object");
			commSocket.sendObject(data);
			Log.d("Communicator", "Sent object");
			return true;
		}
		catch (IOException e) {
			Log.d("Communicator", "Error sending, possibly disconnected");
			socketOpen = false;
			closeCommSocket();
			return false;
		}
	}
	
	public void send(String identifier, Object obj)
	{
		if(!socketOpen) reset();
		
		Log.d("Communicator", "Entering send function");

		Log.d("Communicator", "Entering try block");
		Vector<Object> data = new Vector<Object>();
		data.add(identifier);
		data.add(obj);

		boolean sent = sendObject(data);
		
		if(!sent)
		{
			unsentData.add(data);
		}
	}

	public void initNumber(String myNumber) {
		this.myNumber = myNumber;
	}
}
