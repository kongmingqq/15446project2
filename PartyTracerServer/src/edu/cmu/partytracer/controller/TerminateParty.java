package edu.cmu.partytracer.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.TerminationBean;
import edu.cmu.partytracer.serverThread.ServerSingleton;
import edu.cmu.partytracer.serverThread.Util;

/**
 * Terminate the party and clean the memory
 * @author Xiaojian Huang
 *
 */
public class TerminateParty {
	public static void terminateParty(String partyID) {
		DatagramSocket s;
		try {
			s = new DatagramSocket(Protocol.SERVER_TERM_SEND_PORT);
			Vector<Object> termMsg = new Vector<Object>();
			TerminationBean termBean = new TerminationBean(partyID);
			termMsg.add("TERM");
			termMsg.add(termBean);
			byte[] bs = Util.objToBytes(termMsg);
			//TODO: enable this for the real case
//			for (String eachClient : ServerSingleton.getInstance().clientAddressMap.get(partyID)) {
//				InetAddress ip = InetAddress.getByName(eachClient);
			//TODO: change this IP to the client IP for test
			InetAddress ip = InetAddress.getByName("127.0.0.1");
				DatagramPacket p = new DatagramPacket(bs, bs.length, ip, Protocol.CLIENT_TRACE_RECEIVE_PORT);
				s.send(p);
//			}
			s.close();
			System.out.println("Server sent Terminate Message");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		ServerSingleton.getInstance().curStatus.remove(partyID);
//		ServerSingleton.getInstance().clientAddressMap.remove(partyID);
//		ServerSingleton.getInstance().invitationMap.remove(partyID);
//		ServerSingleton.getInstance().locationCacheMap.remove(partyID);
//		ServerSingleton.getInstance().locationQueueMap.remove(partyID);
//		ServerSingleton.getInstance().voteProcessMap.remove(partyID);
//		ServerSingleton.getInstance().partyTimerThreadMap.remove(partyID);
	}
}
