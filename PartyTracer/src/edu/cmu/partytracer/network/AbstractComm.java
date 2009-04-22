package edu.cmu.partytracer.network;

public abstract class AbstractComm {
	public abstract int getMyNumber();
	public abstract boolean send(int identifier, Object obj);
	public abstract String[] getPhoneBook();
	public abstract int lookUp(String name);
}
