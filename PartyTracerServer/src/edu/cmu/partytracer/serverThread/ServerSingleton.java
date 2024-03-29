package edu.cmu.partytracer.serverThread;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.Location;
import edu.cmu.partytracer.bean.LocationBean;
import edu.cmu.partytracer.controller.PartyTimer;
import edu.cmu.partytracer.controller.VoteTimer;
import edu.cmu.partytracer.model.database.Model;
import edu.cmu.partytracer.model.invitation.Invitation;
import edu.cmu.partytracer.serverThread.ServerUDPThread.ServerCacheQueue;

/**
 * Singleton class for storing all the information in the memeory.
 * @author Xiaojian Huang
 *
 */
public class ServerSingleton {
	//current status for each party
	public HashMap<String, String> curStatus;
	//model which is used to connect to the database
	public Model model;
//	public HashMap<String, HashMap<String, Integer>> voteMap;
	//use for storing all the InvitationBean
	public HashMap<String, InvitationBean> invitationMap;
	//Map used to all the client IP for each party
	public HashMap<String, ArrayList<String>> clientAddressMap;
	//location queue for each party
	public HashMap<String, ServerCacheQueue> locationQueueMap;
	//use to cache the location information which would be sent to the client 
	public HashMap<String, Object[]> locationCacheMap;
	//use to process the vote and get the result
	public HashMap<String, Invitation> voteProcessMap;
	public int Server_UDP_Send;
	public HashMap<String, PartyTimer> partyTimerThreadMap;
	public HashMap<String, VoteTimer> voteTimerThreadMap;

	private static ServerSingleton unique_instance;

	private ServerSingleton() {
		curStatus = new HashMap<String, String>();
		model = new Model("com.mysql.jdbc.Driver", "jdbc:mysql:///partytracer");
//		voteMap = new HashMap<String, HashMap<String, Integer>>();
		invitationMap = new HashMap<String, InvitationBean>();
		clientAddressMap = new HashMap<String, ArrayList<String>>();
		locationQueueMap = new HashMap<String, ServerCacheQueue>();
		locationCacheMap = new HashMap<String, Object[]>();
		voteProcessMap = new HashMap<String, Invitation>();
		partyTimerThreadMap = new HashMap<String, PartyTimer>();
		voteTimerThreadMap = new HashMap<String, VoteTimer>();
		this.Server_UDP_Send = 20000;
	}

	public static synchronized ServerSingleton getInstance() {
		if (unique_instance == null) {
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

//	public HashMap<String, Integer> getVoteMap(String partyID) {
//		return voteMap.get(partyID);
//	}
//
//	public void increaseVote(String partyID, String option) {
//		HashMap<String, Integer> curVoteMap = this.voteMap.get(partyID);
//		int curVoteNum = curVoteMap.get(option);
//		curVoteMap.put(option, curVoteNum + 1);
//		this.voteMap.put(partyID, curVoteMap);
//	}

	public InvitationBean getInvitationBean(String partyID) {
		return invitationMap.get(partyID);
	}

	public void setInvitationBean(String partyID, InvitationBean invitationBean) {
		this.invitationMap.put(partyID, invitationBean);
		HashMap<String, Integer> curVoteMap = new HashMap<String, Integer>();
		for (String option : invitationBean.getOptions()) {
			curVoteMap.put(option, 0);
		}
//		this.voteMap.put(partyID, curVoteMap);
	}

	public float getTimeOut(String partyID) {
		return this.invitationMap.get(partyID).getTimeout();
	}

	public void saveClientAddress(String partyID, String clientIPAddress) {
		ArrayList<String> clientList = clientAddressMap.get(partyID);
		clientList.add(clientIPAddress);
		this.clientAddressMap.put(partyID, clientList);
	}

	public ArrayList<String> getClientList(String partyID) {
		return this.clientAddressMap.get(partyID);
	}

	public void addToLocationQueue(String partyID, LocationBean lb) {
		ServerCacheQueue locationQueue = this.locationQueueMap.get(partyID);
		if (locationQueue == null){
			locationQueue = new ServerCacheQueue();
		}
		locationQueue.enqueue(lb);
		this.locationQueueMap.put(partyID, locationQueue);
	}

	public ServerCacheQueue getLocationQueue(String partyID) {
		return locationQueueMap.get(partyID);
	}

	@SuppressWarnings("unchecked")
	public Object[] getLocationCache(String partyID) {
		return (Object[]) locationCacheMap.get(partyID);
	}

	public void setLocationCache(String partyID, List<Location> locationCache) {
		Object[] tmp = new Object[2];
		tmp[0] = System.currentTimeMillis();
		tmp[1] = locationCache;
		this.locationCacheMap.put(partyID, tmp);
	}
	
	public int getServerUDPPort(){
		this.Server_UDP_Send++;
		return this.Server_UDP_Send;
	}
}
