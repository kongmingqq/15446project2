package edu.cmu.partytracer.serverUnitTest;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import edu.cmu.partytracer.bean.AggLocationBean;
import edu.cmu.partytracer.bean.Bean;
import edu.cmu.partytracer.bean.BeanVector;
import edu.cmu.partytracer.bean.Location;
import edu.cmu.partytracer.bean.LocationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.BeanVector.BeanVectorException;
import edu.cmu.partytracer.dataProcessor.DataDispatcher;
import edu.cmu.partytracer.serverThread.Util;
import edu.cmu.partytracer.serverThread.XMLParser;

public class ClientSendingTest {
	static int STEP = 20;
	static int EPOCH = Protocol.EPOCH;
	static ServerCacheQueue sCache = new ServerCacheQueue();
	static Set<Thread> threads = new HashSet<Thread>();
	static String clientIP;

	public static void main(String[] args) {
		clientIP = "128.237.222.222";
		Thread ss = new ssThread();
		ss.start();
		threads.add(ss);

		XMLParser p = new XMLParser("test.xml");
		XMLParser.Destination d = p.getDestination();
		for(XMLParser.Client c:p.getClientList()) {
			cThread ct = new cThread(c,d);
			ct.start();
			threads.add(ct);
		}

	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	/**
	 * simulate client thread sending location packets
	 * it is only for the purpose of testing
	 */
	public static class cThread extends Thread {
		int desLat;// =40444314;
		int desLng;// =-79942961;
		static int EPOCH = Protocol.EPOCH; //the rhythm of sending packets
		String id;
		
		int lat;
		int lng;
		int step;
		
		int sleepTime;
		int desPort = 8888;
		static int PORT = 10000;
		int port;
		String addr = "127.0.0.1";
		static Random r = new Random();

		public cThread(XMLParser.Client c, XMLParser.Destination d) {
			this.sleepTime = c.getRandomDelay();
			this.step = c.getStep();
			this.id = c.getId();
			this.port = PORT++;
			this.desLat = d.getLatitude();
			this.desLng = d.getLongitude();
			this.lat = c.getLatOffset()+d.getLatitude();
			this.lng = c.getLngOffset()+d.getLongitude();
		}
		public cThread(int sleepTime, int step, String id, int port, int lat, int lng) {
			this.sleepTime = sleepTime;
			this.step = step;
			this.id = id;
			this.port = port;
			this.lat = lat;
			this.lng = lng;
		}
		
		public void run() {

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				interrupt();
			}
			
			int i=0;
			while(i<step){
				if(isInterrupted())	{
					break;
				}
				i++;
				try {
					//add fluctuation to sleep time, simulate propagation delay
					Thread.sleep(EPOCH+(r.nextInt()%sleepTime)-sleepTime/2);
				} catch (InterruptedException e1) {
					interrupt();
				}

				int curlat = lat+i*(desLat-lat)/step;
				int curlng = lng+i*(desLng-lng)/step;
				
				DatagramSocket s;
				try {
					s = new DatagramSocket(port);
					LocationBean lb = new LocationBean(new Location(id,curlat,curlng),false);
			
					byte[] bs = Util.objToBytes(BeanVector.wrapBean(lb));
					InetAddress ip = InetAddress.getByName(addr);
					DatagramPacket p = new DatagramPacket(bs,bs.length,ip,desPort);
					s.send(p);
					s.close();
					System.out.println("Client sent LocationBean");
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * Server sending packets
	 */
	public static class ssThread extends Thread {
		public void run() {
			// delay 3 epoches to wait enough pakcets
			try {
				Thread.sleep(3 * EPOCH);
			} catch (InterruptedException e1) {
				interrupt();
			}

			int i = 0;
			int step = STEP;
			while (i < step) {
				if (isInterrupted()) {
					break;
				}
				i++;
				try {
					Thread.sleep(EPOCH);
				} catch (InterruptedException e1) {
					interrupt();
				}
				List<Location> locs = null;
				if (sCache.size() >= 3) {
					locs = sCache.dequeueLocationBatch(sCache.size() / 3);
					// duplicates are ok to keep, if the list is large and gets
					// split
					// the duplicates act like replicas
				} else {
					i--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						interrupt();
					}
				}
				if (locs != null) {
					DatagramSocket s;
					int port = 8889;
					String addr = clientIP;
					int desPort = 9999;
					try {
						s = new DatagramSocket(port);
						// TODO split large list into smaller ones and send
						// separately
						Bean bp = new AggLocationBean(0, locs);
						byte[] bs = Util.objToBytes(BeanVector.wrapBean(bp));
						InetAddress ip = InetAddress.getByName(addr);
						DatagramPacket p = new DatagramPacket(bs, bs.length, ip, desPort);
						s.send(p);
						s.close();
						System.out.println("Server sent AggLocationBean size " + bs.length);
						System.out.println("CacheSize is " + sCache.size());
					} catch (SocketException e) {
						e.printStackTrace();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Server receiving packets
	 * 
	 * @author KM
	 * 
	 */
	public static class srThread extends Thread {

		public void run() {
			int step = 3 * STEP;

			int i = 0;
			DatagramSocket s = null;
			try {
				s = new DatagramSocket(Protocol.SERVER_TRACE_RECEIVE_PORT);
//				s.setSoTimeout(EPOCH * 3);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
			;
			while (s != null && i < step) {
				if (isInterrupted()) {
					break;
				}
				i++;
				try {
					byte[] bs = new byte[1024];
					DatagramPacket p = new DatagramPacket(bs, bs.length);
					s.receive(p);
					BeanVector bv = new BeanVector(Util.bytesToObj(p.getData()));

					if (bv.getType().equals(Protocol.TYPE_LocationBean)) {
						sCache.enqueue((LocationBean) (bv.getBean()));
						Location loc = ((LocationBean) (bv.getBean())).getLocation();
//						if(ServerSingleton.getInstance().getCurStatus(loc.getId())==null || !ServerSingleton.getInstance().getCurStatus(loc.getId()).equals("VOTE_RESULT_SENT")){
//							continue;
//						}
						System.out.println("Server receiving LocationBean:" + loc);
						System.out.println("IP Address is: " + p.getAddress().getHostAddress());
						DataDispatcher.storeLocationMsg((LocationBean) bv.getBean(), p.getAddress().getHostAddress());
					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (BeanVectorException e) {
					e.printStackTrace();
				}
			}
			if (s != null)
				s.close();
		}

	}

	/**
	 * Priority queue based on timestamp caches 3x epoch data
	 * 
	 * @author KM
	 * 
	 */
	public static class ServerCacheQueue {
		volatile PriorityQueue<LocationBean> queue;

		public ServerCacheQueue() {
			queue = new PriorityQueue<LocationBean>();
		}

		public synchronized List<Location> dequeueLocationBatch(int n) {
			if (n > queue.size())
				n = queue.size();
			if (n == 0)
				return null;
			List<Location> locs = new LinkedList<Location>();

			for (int i = 0; i < n; i++) {
				locs.add(queue.remove().getLocation());
			}

			return locs;
		}

		public synchronized LocationBean dequeue() {
			return queue.remove();
		}

		public synchronized void enqueue(LocationBean lb) {
			// delayed packets will be dropped
			if (queue.size() > 0 && lb.getTime() < queue.peek().getTime()) {
				return;
			}
			queue.add(lb);
		}

		public synchronized int size() {
			return queue.size();
		}
	}
}