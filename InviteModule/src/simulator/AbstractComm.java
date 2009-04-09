package simulator;

public abstract class AbstractComm {
	public abstract int getMyNumber();
	public abstract void send(int identifier, Object obj);
	public abstract int nextEventId();
	public abstract String[] getPhoneBook();
	public abstract int lookUp(String name);
}
