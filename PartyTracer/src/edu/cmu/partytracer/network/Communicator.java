package edu.cmu.partytracer.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class Communicator extends AbstractComm{
	private Socket appSocket;
	
	public Communicator() {
		DataThread serverListener = new DataThread(appSocket);
		serverListener.start();
	}
	
	public int getMyNumber() {
		return 0;
	}

	public boolean send(int identifier, Object obj) {
		try {
			OutputStream rawOut = appSocket.getOutputStream();
			ObjectOutputStream objStream = new ObjectOutputStream(rawOut);
			
			Vector<Object> data = new Vector<Object>();
			data.add(identifier);
			data.add(obj);
			
			objStream.writeObject(data);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	public String[] getPhoneBook() {
		String[] userList = new String[0];
		
		return userList;
	}
	
	public int lookUp(String name)
	{
		return 0;
	}
}
