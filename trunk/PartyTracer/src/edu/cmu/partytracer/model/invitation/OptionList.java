package edu.cmu.partytracer.model.invitation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Eric Foote
 *
 * An OptionList represents a set of options that fall under a single category in an invitation.
 * Each instance of the Invitation class will store a list of these objects, one for each category
 * that users can vote on.
 * 
 * Information about which users have voted for which options are stored in the form of phone numbers.
 * So, to add or remove the votes from a particular user, the user's phone number is passed in as an
 * argument.
 */
public class OptionList {
	private HashMap<String, ArrayList<Integer>> options;
	
	/**
	 * construct a new, empty OptionList
	 */
	public OptionList()
	{
		options = new HashMap<String, ArrayList<Integer>>();
	}
	
	/**
	 * Called whenever a user selects one of the options in this list. addVote marks the fact that
	 * a user has voted for a particular option
	 * 
	 * @param user - phone number of the user who is voting
	 * @param vote - the option that the user has voted for
	 */
	protected void addVote(int user, String vote)
	{
		ArrayList<Integer> voters = options.get(vote);
		if(!voters.contains(user))
		{
			voters.add(user);
			options.put(vote, voters);
		}
	}
	
	/**
	 * Adds a new option to the list, if it doesn't exist already.
	 * 
	 * @param opt - the new option to add.
	 */
	public void addOption(String opt)
	{
		if(!options.containsKey(opt))
		{
			options.put(opt, new ArrayList<Integer>());
		}
	}
	
	/**
	 * Given one of the options in the list, returns the number of users who have voted for it
	 * 
	 * @param option - the option to get the number of votes for
	 * @return - the number of users who have voted for the option
	 */
	public int getNumVotesFor(String option) {
		return options.get(option).size();
	}
	
	/**
	 * Similar to getNumVotesFor, getVotersFor returns a list of phone numbers of all the users who have voted
	 * for a particular option
	 * 
	 * @param option - the option to get the voters for
	 * @return - a list of users who have voted for the option
	 */
	public int[] getVotersFor(String option)
	{
		ArrayList<Integer> voteNumbers = options.get(option);
		int[] voters = new int[voteNumbers.size()];
		
		for(int i=0; i<voters.length; i++)
		{
			voters[i] = voteNumbers.get(i).intValue();
		}
		
		return voters;
	}

	/**
	 * Retrieve a list of all the options that a particular user has voted for.
	 * 
	 * @param user - phone number of the user whose votes to retrieve
	 * @return - an array containing the names of each of the options that the user has selected
	 */
	public String[] getVotesOf(int user) 
	{
		ArrayList<String> votes = new ArrayList<String>();
		String[] optionList = (String[]) options.keySet().toArray();
		
		for(int i=0; i<optionList.length; i++)
		{
			if(options.get(optionList[i]).contains(user))
			{
				votes.add(optionList[i]);
			}
		}
		
		return (String[]) votes.toArray();
	}

	/**
	 * "resets" the voting of a particular user. Removes this user's votes from each option in the list
	 * 
	 * @param user - phone number of the user to remove
	 */
	protected void removeVotesOf(int user) 
	{
		String[] optionList = (String[]) options.keySet().toArray();
		
		for(int i=0; i<optionList.length; i++) {
			options.get(optionList[i]).remove(user);
		}
	}
	
	/**
	 * Called whenever a user selects multiple options in the list, marks the fact that user
	 * 
	 * @param user - phone number of the user who is voting
	 * @param votes - a list of all the options that the user has voted for
	 */
	protected void addVotes(Integer user, String[] votes) 
	{
		for(int i=0; i<votes.length; i++) {
			addVote(user, votes[i]);
		}
	}
	
	/**
	 * get a list of all the options in this list
	 * @return - a list of all possible options that users can vote for
	 */
	public String[] getAllOptions()
	{
		return (String[]) options.keySet().toArray();
	}
}
