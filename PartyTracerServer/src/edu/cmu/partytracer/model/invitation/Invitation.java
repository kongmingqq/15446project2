package edu.cmu.partytracer.model.invitation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.VoteBean;

/**
 * 
 * @author Eric Foote
 * The Invitation class represents an event that can be created by a user. Each Invitation
 * has a name and a description, which are visible to all users and are set when the
 * invitation is created. The Invitation class also stores a list of the phone numnbers
 * of each invited user, and a list of options for users to vote on. When all invited
 * users have voted, or some set timeout has passed, the Invitation is moved to "active"
 * state, and users can no longer vote.
 * 
 * An invitation may also have one or more categories for users to vote on. An example of a
 * category might be a time or place that the users need to decide on; each category can then
 * have one or more options that users can choose from.
 */
public class Invitation {

	public static int TITLE_INDEX = 0;
	public static int DESCRIPTION_INDEX = 1;
	public static int NUM_DATA_ITEMS = 2;
	
	public static String categoryString = "Category";
	public static String voterString = "Voter ";
	public static String endString = "End";
	
	private int iid;
	private String creator;
	private boolean isActive;
	private String title;
	private String description;
	private HashMap<String, OptionList> options;
	private ArrayList<String> invitedUsers;
	private ArrayList<String> votedUsers;
	
	/**
	 * Creates a new invitation
	 * 
	 * @param name a name for the invitation
	 * @param text a short description of the invitation
	 * @param creatingUser the phone number of the user who created this invitation
	 */
	public Invitation(int id, String name, String text, String creatingUser)
	{
		iid = id;
		creator = creatingUser;
		title = name;
		description = text;
		isActive = false;
		options = new HashMap<String, OptionList>();
		invitedUsers = new ArrayList<String>();
		votedUsers = new ArrayList<String>();
		
		invitedUsers.add(creator);
	}

	/**
	 * Adds voting information to this invitation. When a client votes on an event, a VoteBean is sent to the server.
	 * The server can then user that bean's getWhichInvite method to see which invitation the vote goes to, then call this
	 * method on that invite and pass in the voteBean as a parameter.
	 * 
	 * @param vb - a bean representing the vote to add to this invitation
	 */
	public void addVotes(VoteBean vb)
	{
		String[] voters = vb.getVoters();
		
		for(int i=0; i<voters.length; i++)
		{
			addVote(vb, i);
		}
	}
	
	// adds the votes of voter index to this invitation
	private void addVote(VoteBean vb, int index)
	{
		String voter = vb.getVoters()[index];
		
		if(!votedUsers.contains(voter))
			votedUsers.add(voter);
		
		
		Vector<String> voteData = new Vector<String>(Arrays.asList(vb.getData()));
		addVotesFromArray(voter, voteData);
	}
	
	//given an array representing all the data from a vote bean, adds the votes of a specific voter to this
	// invitation
	private void addVotesFromArray(String voter, Vector<String> voteData)
	{
		ArrayList<String> categories = new ArrayList<String>(options.keySet());
		int voterIndex = voteData.indexOf(voterString + voter);
		
		if(voterIndex >= 0)
		{
			for(int i=0; i<categories.size(); i++)
			{
				int catIndex = voteData.indexOf(categories.get(i), voterIndex);
				int catEnd = Math.min(voteData.indexOf(categoryString, catIndex), voteData.indexOf(endString, catIndex));
				
				if(catEnd == -1)
				{
					catEnd = voteData.indexOf(endString, catIndex);
				}
				
				catIndex++;
				
				for(int j=catIndex; j<catEnd; j++)
				{
					options.get(categories.get(i)).addVote(voter, voteData.get(j));
				}
			}
		}
	}
	
	/**
	 * Creates a complete VoteBean containing all of the collected voting information for this invitation. The server can then
	 * send the resulting bean out to all the clients who are invited, and the clients can reconstruct the updated voting information
	 * from that
	 * 
	 * @return a VoteBean containing all the voting information for this event
	 */
	public VoteBean getVotingInfo()
	{
		VoteBean vb = new VoteBean();
		vb.setPartyId(Integer.toString(iid));
		vb.setVoters(getAllVoters());
		vb.setData(createVotingArray());
		
		return vb;
	}
	
	private String[] getAllVoters()
	{
		ArrayList<String> voters = new ArrayList<String>(Arrays.asList(getInvitedUsers()));
		voters.add(creator);
		String[] voterArray = new String[voters.size()];
		
		for(int i=0; i<voters.size(); i++)
		{
			voterArray[i] = voters.get(i);
		}
		
		return voterArray;
	}
	
	private ArrayList<String> addVotesFromUser(String voterId)
	{
		ArrayList<String> userData = new ArrayList<String>();
		userData.add(voterString + voterId);
		ArrayList<String> categories = new ArrayList<String>(options.keySet());
		
		for(int j=0; j<categories.size(); j++)
		{
			OptionList catOptions = options.get(categories.get(j));
			userData.add(categoryString);
			userData.add(categories.get(j));
			
			userData.addAll(Arrays.asList(catOptions.getVotesOf(voterId)));
		}
		return userData;
	}
	
	//Creates an array of vote data that can be stored in a vote bean. The bean can then be sent to the server
	// or another client
	private String[] createVotingArray()
	{
		ArrayList<String> voteData = new ArrayList<String>();
		
		for(int i=0; i<invitedUsers.size(); i++)
		{
			String voterId = invitedUsers.get(i);

			voteData.addAll(addVotesFromUser(voterId));
			voteData.add(endString);
		}
		
		voteData.addAll(addVotesFromUser(creator));
		voteData.add(endString);
		
		String[] votesAsArray = new String[voteData.size()];
		for(int i=0; i<votesAsArray.length; i++)
		{
			votesAsArray[i] = voteData.get(i);
		}
		
		return votesAsArray;
	}
	
	/**
	 * Re-creates an invitation from an invitationBean. The bean is assumed to have been created using
	 * an invitation's toInvitationBean method
	 * 
	 * @param ib - the bean to create a new invitation from
	 * @return an invitation that should be identical to the one that created the bean
	 */
	public static Invitation fromInvitationBean(InvitationBean ib)
	{
		
		int id = Integer.valueOf(ib.getId());
		String creator = ib.getSender();
		String[] details = ib.getData();
		
		Invitation invite = new Invitation(id, details[TITLE_INDEX], details[DESCRIPTION_INDEX], creator);
		
		String[] invited = ib.getInviteList();
		invite.invitedUsers = new ArrayList<String>();
		
		for(int i=0; i<invited.length; i++) {
			invite.invitedUsers.add(invited[i]);
		}
		
		String[] catList = ib.getOptions();
		int mode = 0;
		String catName = "";
		
		for(int i=0; i<catList.length; i++)
		{
			
			if(mode == 0)
			{
				mode = 1;
			}
			else if(mode == 1)
			{
				catName = catList[i];
				mode = 2;
			}
			else if(mode == 2)
			{
				if(!((catList[i].equals(categoryString)) || (catList[i].equals(endString))))
				{
					invite.addOption(catName, catList[i]);
				}
				else
					mode = 1;
			}
		}
		
		invite.isActive = ib.getActive();
		
		return invite;
	}
	
	/**
	 * Creates an invitationBean, containing all the information that identifies this invitation. The static fromInvitationBean
	 * method should then be able to completely reconstruct this invitation from the bean that gets returned
	 * 
	 * @return an invitationBean containing all this invitation's information
	 */
	public InvitationBean toInvitationBean()
	{
		InvitationBean ib = new InvitationBean();

		ib.setId(Integer.toString(iid));
		ib.setSender(creator);
		
		String[] invited = new String[invitedUsers.size()];
		for(int i=0; i<invited.length; i++) {
			invited[i] = invitedUsers.get(i);
		}
		
		ib.setInviteList(invited);
		
		String[] data = new String[NUM_DATA_ITEMS];
		data[TITLE_INDEX] = title;
		data[DESCRIPTION_INDEX] = description;
		
		ib.setData(data);
		ib.setActive(isActive);
		ib.setVoteData(new String[0]);
		
		ArrayList<String> categories = new ArrayList<String>();
		ArrayList<String> headers = new ArrayList<String>(options.keySet());
		for(int i=0; i<headers.size(); i++)
		{
			categories.add(categoryString);
			categories.add(headers.get(i));
			
			
			String[] optList = options.get(headers.get(i)).getAllOptions();
			for(int j=0; j<optList.length; j++)
			{
				categories.add(optList[j]);
			}
		}
		categories.add(endString);
		
		String[] catArray = new String[categories.size()];
		for(int i=0; i<catArray.length; i++)
		{
			catArray[i] = categories.get(i);
		}
		
		ib.setOptions(catArray);
		
		return ib;
	}
	
	/**
	 * The equals method for an invitation tests to see if both invitations represent the same event. Two different versions of the same invitation
	 * (for example, with different voting information or version vectors) will still return as equal.
	 */
	public boolean equals(Object obj)
	{
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		else
		{
			Invitation inviteObj = (Invitation) obj;
			return (iid == inviteObj.iid);
		}
	}
	
	/**
	 * returns a hash code that is consistent with the contract specified in the java Object class: two events that are equal will always
	 * return the same hash code
	 */
	public int hashCode() {
		return iid;
	}
	
	public int getId() {
		return iid;
	}
	public boolean isActive() {
		return isActive;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public HashMap<String, OptionList> getOptions() {
		return options;
	}
	public String[] getInvitedUsers() {
		String[] invites = new String[invitedUsers.size()];
		for(int i=0; i<invites.length; i++)
		{
			invites[i] = invitedUsers.get(i);
		}
		return invites;
	}
	
	/**
	 * adds a new option for users to vote on. If the given category doesn't exist yet, this method will create it and add "option" as the only available
	 * option for this category. Otherwise, "option" will be added as an additional option for the category
	 * 
	 * @param category
	 * @param option
	 */
	public void addOption(String category, String option)
	{
		if(!options.containsKey(category)) {
			options.put(category, new OptionList());
		}

		options.get(category).addOption(option);
	}
	/**
	 * Adds a new user to the list of invited users
	 * 
	 * @param newUser - the phone number of the user to invite
	 */
	public void addInvite(String newUser) {
		if(!invitedUsers.contains(newUser))
		{
			invitedUsers.add(newUser);
		}
	}
	/**
	 * sets this invitation to be an "active" invite instead of a "vote" invite.
	 */
	public void activate() {
		isActive = true;
		
		ArrayList<String> headers = new ArrayList<String>(options.keySet());
		
		for(int i=0; i<headers.size(); i++)
		{
			options.get(headers.get(i)).finalize();
		}
	}
	
	public void assignId(int id)
	{
		this.iid = id;
	}
	
	public int numVotedUsers()
	{
		return votedUsers.size();
	}
}