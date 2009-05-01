package edu.cmu.partytracer.dataProcessor;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;
import edu.cmu.partytracer.bean.LocationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.serverThread.ServerSingleton;
import edu.cmu.partytracer.serverThread.ServerUDPThread.ServerCacheQueue;
import edu.cmu.partytracer.serverThread.ServerUDPThread.ssThread;

/**
 * Communicate with the client
 * 
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
	public static boolean sendVoteOptionInformation(String partyID, String clientIPAddress, Socket clientRequest) {
		try {
			Vector<Object> sendVector = new Vector<Object>();
			ArrayList<String> clientList = new ArrayList<String>();
			InvitationBean invitationBean = ServerSingleton.getInstance().getInvitationBean(partyID);
			invitationBean.setId(partyID);
			String dataType = Protocol.TYPE_InvitationBean;
			sendVector.add(dataType);
			sendVector.add(invitationBean);
			// for(String opt : invitationBean.getOptions())
			// System.out.println("opts: "+opt);
			// System.out.println(clientIPAddress);
			// Socket sendSocket = new Socket(clientIPAddress,
			// Protocol.CLIENT_MODEL_RECEIVE_PORT);
			ObjectOutputStream out = new ObjectOutputStream(clientRequest.getOutputStream());
			out.writeObject(sendVector);
			System.out.println("Sending the options");
			out.flush();
			out.close();
			// clientRequest.close();
			// sendSocket.close();
			System.out.println("Send the invitation Bean to " + clientIPAddress);
			ServerSingleton.getInstance().setCurStatus(partyID, "SENDING_OPTIONS");
			if (ServerSingleton.getInstance().clientAddressMap.get(partyID) != null) {
				clientList = ServerSingleton.getInstance().clientAddressMap.get(partyID);
			}
			clientList.add(clientIPAddress);
			ServerSingleton.getInstance().clientAddressMap.put(partyID, clientList);
			return true;
		} catch (Exception e) {
			System.out.println("Error in sending vote option list(InvitationBean): " + e.getMessage());
			return false;
		}
	}

	/**
	 * Send the vote result too all the client
	 * 
	 * @param partyID
	 */
	public static void sendVoteResult(String partyID, Socket clientRequest) {
		// String voteResult = DataParser.getResult(partyID);
		// if (voteResult == null){
		// voteResult = ServerSingleton.getInstance().get
		// }
		try {
			Vector<Object> sendVector = new Vector<Object>();
			String dataType = Protocol.TYPE_VoteBean;
			sendVector.add(dataType);
			if (ServerSingleton.getInstance().voteProcessMap.get(partyID).numVotedUsers() == 0) {
				System.out.println("No one votes!");
				return;
			}
			sendVector.add(ServerSingleton.getInstance().voteProcessMap.get(partyID).getVotingInfo());
			// Socket sendSocket = new Socket(eachClient,
			// Protocol.CLIENT_MODEL_RECEIVE_PORT);
			ObjectOutputStream out = new ObjectOutputStream(clientRequest.getOutputStream());
			out.writeObject(sendVector);
			out.close();
			// sendSocket.close();
			System.out.println("Send the result to all the clent");
			ServerSingleton.getInstance().setCurStatus(partyID, "VOTE_RESULT_SENT");
		} catch (Exception e) {
			System.out.println("Error in sending vote result: " + e.getMessage());
		}
	}

	/**
	 * Send the location informaiton through UDP to specific client
	 * 
	 * @param partyID
	 * @param clientIPAddress
	 * @param loc
	 */
	public static void sendAggregatedLocation(String partyID, String clientIPAddress, LocationBean loc) {
		if (System.currentTimeMillis() - Long.valueOf(ServerSingleton.getInstance().getLocationCache(partyID)[0].toString()) >= Protocol.EPOCH) {
			System.out.println("updateTime: " + Long.valueOf(ServerSingleton.getInstance().getLocationCache(partyID)[0].toString()));
			System.out.println("Need to update the server cache!");
			ServerCacheQueue serverCacheQueue = ServerSingleton.getInstance().getLocationQueue(partyID);
			ServerSingleton.getInstance().setLocationCache(partyID, serverCacheQueue.dequeueLocationBatch(serverCacheQueue.size() / 2));
			System.out.println("Location queue: " + ServerSingleton.getInstance().getLocationQueue(partyID).size());
		}
		// DatagramSocket s;
		//		
		// try {
		// s = new DatagramSocket(ServerSingleton.serverUDPPort);
		// Bean bp = new AggLocationBean(0,
		// (List<Location>)ServerSingleton.getInstance().getLocationCache(partyID)[1]);
		// byte[] bs = Util.objToBytes(BeanVector.wrapBean(bp));
		// InetAddress ip = InetAddress.getByName(clientIPAddress);
		// DatagramPacket p = new DatagramPacket(bs, bs.length, ip,
		// ServerSingleton.clientUDPPort);
		// s.send(p);
		// s.close();
		// System.out.println("Server sent AggLocationBean size " + bs.length);
		// } catch (SocketException e) {
		// e.printStackTrace();
		// } catch (UnknownHostException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		Thread ss = new ssThread(clientIPAddress, partyID);
		ss.start();
	}

	public static void sendVoteStatus(String partyID, Socket clientRequest) {
		try {
			Vector<Object> sendVector = new Vector<Object>();
			String dataType = Protocol.TYPE_VoteStatus;
			sendVector.add(dataType);
			sendVector.add(ServerSingleton.getInstance().voteProcessMap.get(partyID));
			ObjectOutputStream out = new ObjectOutputStream(clientRequest.getOutputStream());
			out.writeObject(sendVector);
			out.close();
			System.out.println("Send the vote status");
		} catch (Exception e) {
			System.out.println("Error in sending vote status: " + e.getMessage());
		}
	}

}
