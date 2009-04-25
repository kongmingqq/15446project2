package edu.cmu.partytracer.serverThread;

import java.util.HashMap;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.model.database.Model;

public class ServerSingleton {
	HashMap<String, String> curStatus;
	Model model;
	HashMap<String, HashMap<String, Integer>> voteMap;
	HashMap<String, InvitationBean> invitationMap;
	public static final Integer clientPort = 64451;
	
	private static ServerSingleton unique_instance;
	private ServerSingleton(){
		curStatus = new HashMap<String, String>();
		model = new Model("com.mysql.jdbc.Driver", "jdbc:mysql:///partytracer"); ;
		voteMap = new HashMap<String, HashMap<String, Integer>>();
	}
	public static synchronized ServerSingleton getInstance(){
		if (unique_instance == null){
			unique_instance = new ServerSingleton();
		}
		return unique_instance;
	}
	public String getCurStatus(String partyID) {
		return curStatus.get(partyID);
	}
	public void setCurStatus(String partyID, String curStatus) {
		this.curStatus.put(partyID, curStatus);
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public HashMap<String, HashMap<String, Integer>> getVoteMap() {
		return voteMap;
	}
	public void setVoteMap(HashMap<String, HashMap<String, Integer>> voteMap) {
		this.voteMap = voteMap;
	}
	public InvitationBean getInvitationBean(String partyID) {
		return invitationMap.get(partyID);
	}
	public void setInvitationBean(String partyID, InvitationBean invitationBean) {
		this.invitationMap.put(partyID, invitationBean);
	}
}
