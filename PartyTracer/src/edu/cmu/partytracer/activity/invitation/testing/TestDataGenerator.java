package edu.cmu.partytracer.activity.invitation.testing;

import java.util.HashMap;
import java.util.Random;

public class TestDataGenerator {

	public static boolean TEST_MODE_ON = true;
	private static volatile HashMap<String, String> phoneNumbers;
	private static volatile Random r;
	
	public static String sampleID()
	{
		return "000-000-0000";
	}
	public static String[] samplePhoneBook()
	{
		String[] sampleContacts = new String[] {"User A", "User B", "User C", "User D"};
		
		return sampleContacts;
	}
	public static String lookUpContact(String contact)
	{
		if(phoneNumbers == null)
		{
			phoneNumbers = new HashMap<String, String>();
			phoneNumbers.put("User A", "410-304-2118");
			phoneNumbers.put("User B", "300-800-2009");
			phoneNumbers.put("User C", "664-101-3042");
			phoneNumbers.put("User D", "118-300-8002");
		}
		
		return phoneNumbers.get(contact);
	}
	public static int getNewId()
	{
		if(r == null)
			r = new Random();
		
		return r.nextInt();
	}
}
