package edu.cmu.partytracer.bean;

import java.io.Serializable;

public class InvitationBean implements Serializable{
	private static final long serialVersionUID = -9192761827464932529L;

	public int getSender() {
		return sender;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}

	public int[] getInviteList() {
		return inviteList;
	}

	public void setInviteList(int[] inviteList) {
		this.inviteList = inviteList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getTimeout() {
		return timeout;
	}

	public void setTimeout(float timeout) {
		this.timeout = timeout;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String[] getVoteData() {
		return voteData;
	}
	
	public void setVoteData(String[] voteData) {
		this.voteData = voteData;
	}
	
	private int sender;
	private int[] inviteList;
	private int id;
	private float timeout;
	private String[] data;
	private String[] voteData;
	private boolean active;
	
	public InvitationBean(){};
}

