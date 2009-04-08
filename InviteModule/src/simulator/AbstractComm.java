package simulator;

import project.invite.DataObject;

public abstract class AbstractComm {
	public abstract int getMyNumber();
	public abstract void send(int identifier, DataObject obj);
	public abstract DataObject query(DataObject obj);
	public abstract int nextEventId();
	public abstract String[] getPhoneBook();
	public abstract int lookUp(String name);
}
