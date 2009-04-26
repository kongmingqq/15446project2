package edu.cmu.partytracer.bean;

import java.io.Serializable;

public class VoteBean implements Serializable{
	private static final long serialVersionUID = -262396169281409008L;

	public String getWhichInvite() {
		return whichInvite;
	}
	public void setWhichInvite(String whichInvite) {
		this.whichInvite = whichInvite;
	}
	
	public String[] getVoters() {
		return voters;
	}

	public void setVoters(String[] voters) {
		this.voters = voters;
	}
	
	public String[] getData() {
		return data;
	}
	
	public void setData(String[] data) {
		this.data = data;
	}
	
	private String whichInvite;
	private String[] voters;
	private String[] data;
	
	public VoteBean(){};
}

