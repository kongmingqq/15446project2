package edu.cmu.partytracer.network;

import java.util.Vector;

import edu.cmu.partytracer.ptsocket.PTSocket;
import edu.cmu.partytracer.ptsocket.UDPSocket;

public class Communicator extends AbstractComm{
	private PTSocket appSocket;
	private String myNumber;
	
	public Communicator() {
		try 
		{
			appSocket = new UDPSocket("localhost", 15446);
			DataThread serverListener = new DataThread(appSocket);
			serverListener.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMyNumber() {
		return myNumber;
	}

	public boolean send(int identifier, Object obj) {

		try 
		{			
			Vector<Object> data = new Vector<Object>();
			data.add(identifier);
			data.add(obj);
		
			appSocket.sendObject(data);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void initNumber(String myNumber) {
		this.myNumber = myNumber;
	}
}
