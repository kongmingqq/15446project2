package edu.cmu.partytracer.dataProcessor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import edu.cmu.partytracer.bean.AggLocationBean;
import edu.cmu.partytracer.bean.Bean;
import edu.cmu.partytracer.bean.BeanVector;
import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.Location;
import edu.cmu.partytracer.bean.LocationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.serverThread.ServerSingleton;
import edu.cmu.partytracer.serverThread.Util;
import edu.cmu.partytracer.serverThread.ServerUDPThread.ServerCacheQueue;
import edu.cmu.partytracer.serverThread.ServerUDPThread.ssThread;

/**
 * Communicate with the client 
 * @author Xiaojian Huang
 *
 */
public class ClientCommunicator {
	
	/**
	 * Send the options for voting to specified client
	 * 
	 * @param partyID
	 * @param clientIPAddress
	 */
	public static boolean sendVoteOptionInformation(String partyID, String clientIPAddress) {
		try{
			Vector<Object> sendVector = new Vector<Object>();
			InvitationBean invitationBean = ServerSingleton.getInstance().getInvitationBean(partyID);
			invitationBean.setId(partyID);
			String dataType = Protocol.TYPE_InvitationBean;
			sendVector.add(dataType);
			sendVector.add(invitationBean);
			System.out.println(clientIPAddress);
			Socket sendSocket = new Socket(clientIPAddress, Protocol.CLIENT_MODEL_RECEIVE_PORT);
			ObjectOutputStream out = new ObjectOutputStream(sendSocket.getOutputStream());
			out.writeObject(sendVector);
			out.close();
			sendSocket.close();
			System.out.println("Send the invitation Bean to "+clientIPAddress);
			ServerSingleton.getInstance().setCurStatus(partyID, "SENDING_OPTIONS");
			return true;
		}catch(Exception e){
			System.out.println("Error in sending vote option list(InvitationBean): "+e.getMessage());
			return false;
		}
	}

	/**
	 * Send the vote result too all the client
	 * @param partyID
	 */
	public static void sendVoteResult(String partyID) {
//		String voteResult = DataParser.getResult(partyID);
//		if (voteResult == null){
//			voteResult = ServerSingleton.getInstance().get
//		}
		try{
			Vector<Object> sendVector = new Vector<Object>();
			String dataType = Protocol.TYPE_VoteBean;
			sendVector.add(dataType);
			if(ServerSingleton.getInstance().voteProcessMap.get(partyID).numVotedUsers() ==0){
				System.out.println("No one votes!");
				return;
			}
			sendVector.add(ServerSingleton.getInstance().voteProcessMap.get(partyID).getVotingInfo());
			for (String eachClient : ServerSingleton.getInstance().getClientList(partyID)){
				Socket sendSocket = new Socket(eachClient, Protocol.CLIENT_MODEL_RECEIVE_PORT);
				ObjectOutputStream out = new ObjectOutputStream(sendSocket.getOutputStream());
				out.writeObject(sendVector);
				out.close();
				sendSocket.close();
			}
			System.out.println("Send the result to all the clent");
			ServerSingleton.getInstance().setCurStatus(partyID, "VOTE_RESULT_SENT");
		}catch(Exception e){
			System.out.println("Error in sending vote result: "+e.getMessage());
		}
	}

	/**
	 * Send the location informaiton through UDP to specific client
	 * @param partyID
	 * @param clientIPAddress
	 * @param loc
	 */
	public static void sendAggregatedLocation(String partyID, String clientIPAddress,  LocationBean loc) {
		if (ServerSingleton.getInstance().getLocationCache(partyID)==null || Long.valueOf(ServerSingleton.getInstance().getLocationCache(partyID)[0].toString())-System.currentTimeMillis()>5000){
			System.out.println("Need to update the server cache!");
			ServerCacheQueue serverCacheQueue= ServerSingleton.getInstance().getLocationQueue(partyID);
			ServerSingleton.getInstance().setLocationCache(partyID, serverCacheQueue.dequeueLocationBatch(serverCacheQueue.size()/2));
			return;
		}
//		DatagramSocket s;
//		
//		try {
//			s = new DatagramSocket(ServerSingleton.serverUDPPort);
//			Bean bp = new AggLocationBean(0, (List<Location>)ServerSingleton.getInstance().getLocationCache(partyID)[1]);
//			byte[] bs = Util.objToBytes(BeanVector.wrapBean(bp));
//			InetAddress ip = InetAddress.getByName(clientIPAddress);
//			DatagramPacket p = new DatagramPacket(bs, bs.length, ip, ServerSingleton.clientUDPPort);
//			s.send(p);
//			s.close();
//			System.out.println("Server sent AggLocationBean size " + bs.length);
//		} catch (SocketException e) {
//			e.printStackTrace();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		Thread ss = new ssThread(clientIPAddress, partyID);
		ss.start();
	}
	
}
