package edu.cmu.partytracer.bean;

import java.io.Serializable;

public class VoteBean implements Serializable{
	private static final long serialVersionUID = -7114161707040937518L;

	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
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
	
	private String partyId;
	private String[] voters;
	private String[] data;
	
	public VoteBean(){};
}

