package edu.cmu.partytracer.model.invitation;

public class UserSingleton {
	private static volatile User user;
	
	public static User getUser()
	{
		if(user == null)
			user = new User();
		return user;
	}
	
}
