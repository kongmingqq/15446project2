package edu.cmu.partytracer.model.invitation;

import java.util.ArrayList;

import android.util.Log;

import edu.cmu.partytracer.bean.VoteBean;

import edu.cmu.partytracer.network.ComWrapper;

/**
 * 
 * @author Eric Foote
 *
 * The user class represents a single user of our system. Only one instance of this
 * class should be created per android unit. The User class stores the phone number
 * of this user as well as a list of this user's invitations
 */
public class User {
	private String myNumber;
	private ArrayList<Invitation> myInvites;
	private static String USER_TAG = "User Class";
	
	public User()
	{
		myNumber = ComWrapper.getComm().getMyNumber();
		myInvites = new ArrayList<Invitation>();
	}
	
	/**
	 * 
	 * @return a list of all the invitations that this user has
	 */
	public Invitation[] getMyInvites() {
		Invitation[] invitesAsArray = new Invitation[myInvites.size()];
		
		for(int i=0; i<invitesAsArray.length; i++)
		{
			invitesAsArray[i] = myInvites.get(i);
		}
		
		return invitesAsArray;
	}
	/**
	 * 
	 * @return this user's phone number
	 */
	public String getNumber() {
		return myNumber;
	}
	
	/**
	 * Adds a new invitation to the list of this user's invitations
	 * @param invite -- the invitation to add
	 */
	public void addInvite(Invitation invite) {
		Log.d(USER_TAG, "Adding a new invitation");
		myInvites.add(invite);
	}
	
	/**
	 * Takes the voting data given by a vote bean and adds it to the appropriate invite for this user
	 * 
	 * @param vb - the bean containing voting data to add
	 */
	public void addVote(VoteBean vb)
	{
		int invId = Integer.valueOf(vb.getPartyId());
		Log.d(USER_TAG, "Received a vote for invitation " + invId);
		
		for(int i=0; i<myInvites.size(); i++)
		{
			if(myInvites.get(i).getId() == invId)
			{
				myInvites.get(i).addVotes(vb);
				return;
			}
		}
	}
	
	public String getNameOf(int inviteId)
	{
		for(int i=0; i<myInvites.size(); i++)
		{
			if(myInvites.get(i).getId() == inviteId)
				return myInvites.get(i).getTitle();
		}
		
		return "";
	}
	
	public boolean isInvitedTo(Invitation invite)
	{
		return myInvites.contains(invite);
	}
	
	public void activateEvent(int invId)
	{
		for(int i=0; i<myInvites.size(); i++)
		{
			if(myInvites.get(i).getId() == invId)
				myInvites.get(i).activate();
		}
	}
}

