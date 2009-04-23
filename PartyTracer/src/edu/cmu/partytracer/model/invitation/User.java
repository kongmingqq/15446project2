package edu.cmu.partytracer.model.invitation;

import java.util.ArrayList;

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
		return (Invitation[]) myInvites.toArray();
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
		myInvites.add(invite);
	}
	
	/**
	 * Takes the voting data given by a vote bean and adds it to the appropriate invite for this user
	 * 
	 * @param vb - the bean containing voting data to add
	 */
	public void addVote(VoteBean vb)
	{
		int invId = vb.getWhichInvite();
		
		for(int i=0; i<myInvites.size(); i++)
		{
			if(myInvites.get(i).getId() == invId)
			{
				myInvites.get(i).addVotes(vb);
				return;
			}
		}
	}
	
	public boolean isInvitedTo(Invitation invite)
	{
		return myInvites.contains(invite);
	}
}

