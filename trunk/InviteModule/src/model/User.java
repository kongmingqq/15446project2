package model;

import java.util.ArrayList;

import simulator.ComWrapper;

/**
 * 
 * @author Eric Foote
 *
 * The user class represents a single user of our system. Only one instance of this
 * class should be created per android unit. The User class stores the phone number
 * of this user as well as a list of this user's invitations
 */
public class User {
	private int myNumber;
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
	public int getNumber() {
		return myNumber;
	}
	
	/**
	 * Adds a new invitation to the list of this user's invitations
	 * @param invite -- the invitation to add
	 */
	public void addInvite(Invitation invite) {
		myInvites.add(invite);
	}
}
