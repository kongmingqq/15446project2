package edu.cmu.partytracer.network;

import java.io.IOException;
import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.VoteBean;
import edu.cmu.partytracer.ptsocket.TCPSocket;

import android.util.Log;

public class DataThread extends Thread {

	private TCPSocket inputSocket;
	public volatile boolean shouldRun = true;
	
	public DataThread(TCPSocket s)
	{
		inputSocket = s;
	}
	
	private void closeInputSocket()
	{
		try {
			inputSocket.close();
		} catch (IOException e) {

		}
	}
	
	private void threadSleep(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch(InterruptedException e)
		{
			Log.d("Data Thread", "Data Thread Process Was Interrupted");
		}
	}
	
	private void forwardObject(Object obj)
	{
		Vector<Object> dataIn = new Vector<Object>();
		
		if(obj instanceof Vector)
		{
			dataIn = (Vector<Object>) obj;
			Log.d("Data Thread", "Received a message");
			MessageHandler.forward(dataIn);
		}
		else if(obj instanceof InvitationBean)
		{
			InvitationBean ib = (InvitationBean) obj;
			Log.d("Data Thread", "Received an invitation bean");
			MessageHandler.forward(ib);
		}
		else if(obj instanceof VoteBean)
		{
			VoteBean vb = (VoteBean) obj;
			Log.d("Data Thread", "Received a vote bean");
			MessageHandler.forward(vb);
		}
		else
		{
			Log.d("Data Thread", "Received an unrecognizable message");
			Log.d("Data Thread", obj.toString());
		}
	}
	
	public void run()
	{		
		Log.d("Data Thread", "Entering run function");
		
		while(shouldRun)
		{
			Log.d("Data Thread", "Checking for incoming messages");
			try {
				Object next;
				next = inputSocket.receiveObject();
				forwardObject(next);
			} catch (IOException e1) {
				Log.d("Data Thread", "Error receiving, possibly disconnected");
			} catch (ClassNotFoundException e1) {
				Log.d("Data Thread", "Received an unrecognizable message");
				Log.d("Data Thread", e1.toString());
			}
			
			threadSleep(1000);
		}
		
		closeInputSocket();
	}
}
