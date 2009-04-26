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
import edu.cmu.partytracer.bean.ResultBean;
import edu.cmu.partytracer.serverThread.ServerSingleton;
import edu.cmu.partytracer.serverThread.Util;
import edu.cmu.partytracer.serverThread.ServerUDPThread.ServerCacheQueue;

public class ClientCommunicator {
	public static void sendVoteOptionInformation(String partyID, String clientIPAddress) {
		try{
			Vector<Object> sendVector = new Vector<Object>();
			InvitationBean invitationBean = ServerSingleton.getInstance().getInvitationBean(partyID);
			invitationBean.setId(partyID);
			String dataType = "VOTEINFO";
			sendVector.add(dataType);
			sendVector.add(invitationBean);
			Socket sendSocket = new Socket(clientIPAddress, ServerSingleton.clientPort);
			ObjectOutputStream out = new ObjectOutputStream(sendSocket.getOutputStream());
			out.writeObject(sendVector);
			out.close();
			sendSocket.close();
			System.out.println("Send the invitation Bean to "+clientIPAddress);
			ServerSingleton.getInstance().setCurStatus(partyID, "SENDING_OPTIONS");
		}catch(Exception e){
			System.out.println("Error in sending vote option list(InvitationBean): "+e.getMessage());
		}
	}

	public static void sendVoteResult(String partyID) {
		String voteResult = DataParser.getResult(partyID);
		try{
			Vector<Object> sendVector = new Vector<Object>();
			ResultBean resultBean = new ResultBean();
			String dataType = "VOTERESULT";
			sendVector.add(dataType);
			resultBean.setPartyID(partyID);
			resultBean.setVoteResult(voteResult);
			sendVector.add(resultBean);
			for (String eachClient : ServerSingleton.getInstance().getClientList(partyID)){
				Socket sendSocket = new Socket(eachClient, ServerSingleton.clientPort);
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

	public static void sendAggregatedLocation(String partyID, String clientIPAddress,  LocationBean loc) {
		if (ServerSingleton.getInstance().getLocationCache(partyID)==null || Integer.valueOf(ServerSingleton.getInstance().getLocationCache(partyID)[0].toString())-System.currentTimeMillis()>5000){
			ServerCacheQueue serverCacheQueue= ServerSingleton.getInstance().getLocationQueue(partyID);
			ServerSingleton.getInstance().setLocationCache(partyID, serverCacheQueue.dequeueLocationBatch(serverCacheQueue.size()/2));
		}
		DatagramSocket s;
		try {
			s = new DatagramSocket(ServerSingleton.serverUDPPort);
			// TODO split large list into smaller ones and send
			// separately
			Bean bp = new AggLocationBean(0, (List<Location>)ServerSingleton.getInstance().getLocationCache(partyID)[1]);
			byte[] bs = Util.objToBytes(BeanVector.wrapBean(bp));
			InetAddress ip = InetAddress.getByName(clientIPAddress);
			DatagramPacket p = new DatagramPacket(bs, bs.length, ip, ServerSingleton.clientUDPPort);
			s.send(p);
			s.close();
			System.out.println("Server sent AggLocationBean size " + bs.length);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
