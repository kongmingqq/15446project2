package edu.cmu.partytracer.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import edu.cmu.partytracer.serverThread.ServerSingleton;
import edu.cmu.partytracer.serverThread.Util;

/**
 * Terminate the party and clean the memory
 * @author Xiaojian Huang
 *
 */
public class TerminateParty {
	public static void terminatePary(String partyID) {
		DatagramSocket s;
		try {
			s = new DatagramSocket(ServerSingleton.serverUDPPort);
			// TODO split large list into smaller ones and send
			// separately
			String termMsg = "TERM";
			byte[] bs = Util.objToBytes(termMsg);
			for (String eachClient : ServerSingleton.getInstance().clientAddressMap.get(partyID)) {
				InetAddress ip = InetAddress.getByName(eachClient);
				DatagramPacket p = new DatagramPacket(bs, bs.length, ip, ServerSingleton.clientUDPPort);
				s.send(p);
			}
			s.close();
			System.out.println("Server sent Terminate Message");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ServerSingleton.getInstance().curStatus.remove(partyID);
//		ServerSingleton.getInstance().voteMap.remove(partyID);
		ServerSingleton.getInstance().clientAddressMap.remove(partyID);
		ServerSingleton.getInstance().invitationMap.remove(partyID);
		ServerSingleton.getInstance().locationCacheMap.remove(partyID);
		ServerSingleton.getInstance().locationQueueMap.remove(partyID);
		ServerSingleton.getInstance().voteProcessMap.remove(partyID);
	}
}
