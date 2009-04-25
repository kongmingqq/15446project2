package edu.cmu.partytracer.bean;

public class VoteBean {
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
	private String partyID;
	private String myVote;
	public String getMyVote() {
		return myVote;
	}
	public void setMyVote(String myVote) {
		this.myVote = myVote;
	}
	public String getPartyID() {
		return partyID;
	}
	public void setPartyID(String partyID) {
		this.partyID = partyID;
	}
	public VoteBean(){};
}

