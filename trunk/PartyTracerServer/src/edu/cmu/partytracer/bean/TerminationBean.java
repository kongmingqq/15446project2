package edu.cmu.partytracer.bean;

import java.io.Serializable;

public class TerminationBean extends Bean implements Serializable{
	private static final long serialVersionUID = 208336260564027949L;
	private String id; //phone id
	private String partyId;

	public TerminationBean(String partyId) {
		this(null, partyId);
	}
	
	public TerminationBean(String id, String partyId) {
		this.id = id;
		this.partyId = partyId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
}
