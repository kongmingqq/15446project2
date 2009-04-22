package edu.cmu.partytracer.network;

public class DummyCommunicator extends AbstractComm{

	public int getMyNumber() {
		return 0;
	}

	public String[] getPhoneBook() {
		String[] sampleUsers = new String[3];
		
		sampleUsers[0] = "User A";
		sampleUsers[1] = "User B";
		sampleUsers[2] = "User C";
		
		return sampleUsers;
	}

	public int lookUp(String name) {
		if(name == "User A") return 0;
		else if(name == "User B") return 1;
		else return 2;
	}

	public boolean send(int identifier, Object obj) {
		return true;
	}

}
