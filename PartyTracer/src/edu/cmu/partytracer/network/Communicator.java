package edu.cmu.partytracer.network;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import android.util.Log;

public class Communicator extends AbstractComm{
	private Socket outputSocket;
	private Socket inputSocket;
	private String myNumber;
	
	public Communicator() {
		try
		{
			Log.d("Communicator", "Creating TCP Socket");
			//appSocket = new TCPSocket("128.237.254.154", 15446);
			inputSocket = new Socket("128.237.254.154", 15446);
			Log.d("Communicator", "Finished creating TCP Socket");
			DataThread serverListener = new DataThread(inputSocket);
			serverListener.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMyNumber() {
		return myNumber;
	}

	private boolean send_once(String identifier, Object obj)
	{
		try 
		{			
			Vector<Object> data = new Vector<Object>();
			data.add(identifier);
			data.add(obj);
//		
//			appSocket.sendObject(data);

			outputSocket = new Socket("128.237.254.154", 15446);
			ObjectOutputStream objStream = new ObjectOutputStream(outputSocket.getOutputStream());
			objStream.writeObject(data);
			outputSocket.close();
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void send(String identifier, Object obj) {
		send_once(identifier, obj);
	}

	public void initNumber(String myNumber) {
		this.myNumber = myNumber;
	}
}
