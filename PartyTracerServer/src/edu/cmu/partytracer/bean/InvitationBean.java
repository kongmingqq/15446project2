package edu.cmu.partytracer.bean;

import java.io.Serializable;

public class InvitationBean implements Serializable{

	private static final long serialVersionUID = -5417217001662355679L;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String[] getInviteList() {
		return inviteList;
	}

	public void setInviteList(String[] inviteList) {
		this.inviteList = inviteList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
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
	
	public String[] getOptions() {
		return options;
	}
	
	public void setOptions(String[] options) {
		this.options = options;
	}
	
	private String sender;
	private String[] inviteList;
	private String id;
	private long timeout;
	private String[] data;
	private String[] voteData;
	private boolean active;
	private String[] options;
	
	public InvitationBean(){};
}

