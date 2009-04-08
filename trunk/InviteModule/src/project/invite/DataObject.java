package project.invite;

public class DataObject {
	
	public static int INVITATION = 1;
	public static int QUERY_INVITES = 2;
	public static int CHANGE_VOTE = 3;

	private int type;
	private String[] data;
	
	public DataObject(int t, String[] d)
	{
		type = t;
		data = d;
	}
	
	public int getType() {
		return type;
	}
	public String[] getData() {
		return data;
	}
}
