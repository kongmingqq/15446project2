package edu.cmu.partytracer.activity.trace;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import edu.cmu.partytracer.bean.*;
import edu.cmu.partytracer.bean.BeanVector.BeanVectorException;
import edu.cmu.partytracer.model.trace.CacheQueue;
import edu.cmu.partytracer.ptsocket.*;

import android.app.Activity;
import android.util.Log;

public class tempTest {
    static int STEP = 20;
    static int EPOCH = Protocol.EPOCH;
    static ServerCacheQueue sCache = new ServerCacheQueue();
    static CacheQueue cache = Map.CACHE;
	static Set<Thread> threads = new HashSet<Thread>();
	
    public static void test() {
    	/*
		Thread sr = new srThread();
		sr.start();
		Thread ss = new ssThread();
		ss.start();
        threads.add(sr);
        threads.add(ss);
        
		cThread ct1 = new cThread(20,14,"Phone1",10001,40443611,-79927962);
		cThread ct2 = new cThread(40,20,"Phone2",10002,40439548,-80009308);
		cThread ct3 = new cThread(30,18,"Phone3",10003,40459548,-79926308);
		ct1.start();
		ct2.start();
		ct3.start();
		threads.add(ct1);
		threads.add(ct2);
		threads.add(ct3);
		*/
        Thread t1 = new ReceiveLocation();
		t1.start();
		threads.add(t1);
    }
    
    public static void stop() {
    	for(Thread t:threads) {
    		t.interrupt();
    	}
    }
    
	/**
     * UDP socket keep receiving and enqueue
     * @author KM
     *
     */
    public static class ReceiveLocation extends Thread {
		public void run() {
			int step = STEP;
			int port = 9999;
			
			PTSocket cr = null;
			try {
				cr = new UDPSocket(port);
				//TODO set timeout for PTSocket interface
				((UDPSocket)cr).setTimeout(EPOCH*10);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println(">>>>>>>>");
			while(cr!=null) {
				if(isInterrupted())	{
					Log.v("####","thread ends ReceiveLocation ");
					break;
				}
				System.out.println(">>>>>>>>while");
				Object o = null;
				try {
					System.out.println(">>>>>>>>try to receive");
					o = cr.receiveObject();
				} catch (Exception e) {
					e.printStackTrace();
					//TODO add counter,not break just for once
					break;
				}
				BeanVector bv = null;
				try {
					if(o!=null) {
						System.out.println(">>>>>>>>bv");
						bv = new BeanVector(o);
					}
				} catch (BeanVectorException e) {
					e.printStackTrace();
				}
				if(bv!=null&&bv.getType().equals(Protocol.TYPE_AggLocationBean)) {
					cache.enqueue((AggLocationBean)(bv.getBean()));
					System.out.println(">>>>>>>>Client received agglocation and cached");
				}
			}
			if(cr!=null) {
				try {
					System.out.println(">>>>>>>>crclose");
					cr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.print("Leaving");
	        
		}
    }	
    
    
    
    //------------------------------------------------------------------------
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
    /**
     * simulate client thread sending location packets
     */
	public static class cThread extends Thread {
		int desLat =40444314;
		int desLng =-79942961;
		static int EPOCH = Protocol.EPOCH; //the rhythm of sending packets
		String id;
		
		int lat;
		int lng;
		int step;
		
		int sleepTime;
		int desPort = 8888;
		int port;
		String addr = "127.0.0.1";
		static Random r = new Random();
		
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
					Log.v("####","thread ends cThread ");
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
					LocationBean lb = new LocationBean(new Location(id,curlat,curlng),false, Integer.toString(0));
			
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
			//delay 3 epoches to wait enough pakcets
			try {
				Thread.sleep(3*EPOCH);
			} catch (InterruptedException e1) {
				interrupt();
			}
			
			int i=0;
			int step = STEP;
			while(i<step){
				if(isInterrupted())	{
					Log.v("####","thread ends ssThread ");
					break;
				}
				i++;
				try {
					Thread.sleep(EPOCH);
				} catch (InterruptedException e1) {
					interrupt();
				}
				List<Location> locs = null;
				if(sCache.size()>=3) {
					locs = sCache.dequeueLocationBatch(sCache.size()/3);
					// duplicates are ok to keep, if the list is large and gets split 
					// the duplicates act like replicas
				} else {
					i--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						interrupt();
					}
				}
				if(locs!=null) {
					DatagramSocket s;
					int port = 8889;
					String addr = "127.0.0.1";
					int desPort = 9999;
					try {
						s = new DatagramSocket(port);
						//TODO split large list into smaller ones and send separately
						Bean bp = new AggLocationBean(Integer.toString(0), locs);
						byte[] bs = Util.objToBytes(BeanVector.wrapBean(bp));
						InetAddress ip = InetAddress.getByName(addr);
						DatagramPacket p = new DatagramPacket(bs,bs.length,ip,desPort);
						s.send(p);
						s.close();
						System.out.println("Server sent AggLocationBean size "+bs.length);
						System.out.println("CacheSize is "+sCache.size());
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
	 * @author KM
	 *
	 */
	public static class srThread extends Thread {
		
		public void run() {
			int step = 3*STEP;
			int port = 8888;
			
			int i=0;
			DatagramSocket s=null;
			try {
				s = new DatagramSocket(port);
				s.setSoTimeout(EPOCH*3);
			} catch (SocketException e1) {
				e1.printStackTrace();
			};
			while(s!=null && i<step){
				if(isInterrupted())	{
					Log.v("####","thread ends srThread ");
					break;
				}
				i++;
				try {
					byte[] bs = new byte[1024];
					DatagramPacket p = new DatagramPacket(bs,bs.length);
					s.receive(p);
					BeanVector bv = new BeanVector(Util.bytesToObj(p.getData()));
					
					if(bv.getType().equals(Protocol.TYPE_LocationBean)) {
						sCache.enqueue((LocationBean)(bv.getBean()));
						Location loc = ((LocationBean)(bv.getBean())).getLocation();
						System.out.print("Server receiving LocationBean:");
						System.out.println(loc);
						
					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (BeanVectorException e) {
					e.printStackTrace();
				}
			}
			if(s!=null)	s.close();
		}
		
	}
	/**
	 * Priority queue based on timestamp
	 * caches 3x epoch data
	 * @author KM
	 *
	 */
	public static class ServerCacheQueue {
		volatile PriorityQueue<LocationBean> queue;
		
		public ServerCacheQueue() {
			queue = new PriorityQueue<LocationBean>();
		}
		
		public synchronized List<Location> dequeueLocationBatch(int n){
			if(n>queue.size()) n=queue.size();
			if(n==0) return null;
			List<Location> locs = new LinkedList<Location>();
			
			for(int i=0;i<n;i++) {
				locs.add(queue.remove().getLocation());
			}
			
			return locs;
		}

		public synchronized LocationBean dequeue() {
			return queue.remove();
		}
		
		public synchronized void enqueue(LocationBean lb) {
			//delayed packets will be dropped
			if(queue.size()>0 && lb.getTime()<queue.peek().getTime()) {
				return;
			}
			queue.add(lb);
		}
		
		public synchronized int size() {
			return queue.size();
		}
	}
}