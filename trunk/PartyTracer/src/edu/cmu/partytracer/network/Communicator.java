package edu.cmu.partytracer.network;

import java.io.IOException;
import java.util.HashMap;
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
	private HashMap<String, QueryThread> qThreads;
	
	public Communicator() {
		socketOpen = false;
		unsentData = new Vector<Object>();
		qThreads = new HashMap<String, QueryThread>();
		reset();
	}
	
	public String getMyNumber() {
		return myNumber;
	}

	public void startQueryThread(String inviteId)
	{
		Log.d("Communicator", "Starting query thread for invite " + inviteId);
		QueryThread qThread = new QueryThread(inviteId);
		qThreads.put(inviteId, qThread);
		qThread.start();
	}
	
	public void stopQueryThread(String inviteId)
	{
		ComWrapper.finishedQuerying = true;
	}
	
	public void reset()
	{
		try
		{
			commSocket = new TCPSocket(Application.SERVER_IP, 15446);
			
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
			commSocket.sendObject(data);
			return true;
		}
		catch (IOException e) {
			Log.d("Communicator", "Error sending, possibly disconnected");
			Log.d("Commmunicator", e.toString());
			socketOpen = false;
			closeCommSocket();
			return false;
		}
	}
	
	public void send(String identifier, Object obj)
	{
		if(!socketOpen) reset();
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
