package edu.cmu.partytracer.network;

public abstract class AbstractComm {
	public abstract String getMyNumber();
	public abstract void send(String identifier, Object obj);
	public abstract void initNumber(String myNumber);
}
