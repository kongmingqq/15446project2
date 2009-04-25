package edu.cmu.partytracer.bean;

import java.io.Serializable;

public class InvitationBean implements Serializable{

	private static final long serialVersionUID = 3711636959640384703L;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
	private int id;
	private long timeout;
	private String[] data;
	private String[] voteData;
	private boolean active;
	private String[] options;
	
	public InvitationBean(){};
}

