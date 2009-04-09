package simulator;

public class SimComm extends AbstractComm {

	private int eid;
	
	public SimComm() {
		eid = 0;
	}
	
	public int getMyNumber() {
		return 0;
	}

	public void send(int identifier, Object obj) {

	}

	public int nextEventId() {
		eid++;
		return eid;
	}

	public String[] getPhoneBook() {
		String[] userList = new String[3];
		
		userList[0] = "Larry";
		userList[1] = "Curly";
		userList[2] = "Moe";
		
		return userList;
	}
	
	public int lookUp(String name)
	{
		if(name.equals("Larry")) return 1;
		if(name.equals("Curly")) return 2;
		if(name.equals("Moe")) return 3;
		
		return 4;
	}

}
