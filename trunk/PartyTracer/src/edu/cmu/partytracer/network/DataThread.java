package edu.cmu.partytracer.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.Vector;

public class DataThread extends Thread {

	private Socket inputSocket;
	private boolean crashed;
	
	public DataThread(Socket s)
	{
		crashed = false;
	}
	
	public boolean isCrashed() {
		return crashed;
	}
	
	public void run()
	{
		crashed = false;
		
		try {
			ObjectInputStream objStream = new ObjectInputStream(inputSocket.getInputStream());
			Vector<Object> dataIn = new Vector<Object>();
			
			while((dataIn = (Vector<Object>) objStream.readObject()) != null)
			{
				MessageHandler.forward(dataIn);
			}
		} catch (StreamCorruptedException e) {
			crashed = true;
			return;
		} catch (IOException e) {
			crashed = true;
			return;
		}
		catch (ClassNotFoundException e) {
			crashed = true;
			return;
		}
		
	}
}
