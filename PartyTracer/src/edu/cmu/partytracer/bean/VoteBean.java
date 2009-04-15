package edu.cmu.partytracer.bean;

import java.io.Serializable;

public class VoteBean implements Serializable{
	private static final long serialVersionUID = -1946436334567959551L;

	public int getWhichInvite() {
		return whichInvite;
	}
	public void setWhichInvite(int whichInvite) {
		this.whichInvite = whichInvite;
	}
	
	public int[] getVoters() {
		return voters;
	}

	public void setVoters(int[] voters) {
		this.voters = voters;
	}
	
	public String[] getData() {
		return data;
	}
	
	public void setData(String[] data) {
		this.data = data;
	}
	
	private int whichInvite;
	private int[] voters;
	private String[] data;
	
	public VoteBean(){};
}

