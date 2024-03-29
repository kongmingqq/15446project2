package edu.cmu.partytracer.activity.trace;

import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;
import edu.cmu.partytracer.Application;
import edu.cmu.partytracer.bean.AggLocationBean;
import edu.cmu.partytracer.bean.BeanVector;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.BeanVector.BeanVectorException;
import edu.cmu.partytracer.ptsocket.PTSocket;
import edu.cmu.partytracer.ptsocket.UDPSocket;
import edu.cmu.partytracer.model.trace.CacheQueue;

public class TraceReceiveThread extends Thread {
	static int EPOCH = Protocol.EPOCH;
	static CacheQueue CACHE = Application.TRACE_CACHE;
	static int PORT = Protocol.CLIENT_TRACE_RECEIVE_PORT;
	Map mapActivity;
	public TraceReceiveThread(Map mapActivity) {
		this.mapActivity = mapActivity;
	}
	
	public void run() {
		PTSocket cr = null;
		try {
			cr = new UDPSocket(PORT);
			//TODO set timeout for PTSocket interface
			((UDPSocket)cr).setTimeout(EPOCH*10);
		} catch (Exception e) {
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
			if(bv!=null) {
				if(bv.getType().equals(Protocol.TYPE_AggLocationBean)) {
					CACHE.enqueue((AggLocationBean)(bv.getBean()));
					System.out.println(">>>>>>>>Client received agglocation and cached");
				} else if(bv.getType().equals(Protocol.TYPE_TerminationBean)) {
					//TODO termination, need access MAP activity to finish it?
					if(mapActivity!=null) {
						mapActivity.terminate("The server has terminated the party tracing",false);
					} else {
				    	if(Application.TRACE_SEND_THREAD!=null) {
					    	Application.TRACE_SEND_THREAD.interrupt();
					    	Application.TRACE_SEND_THREAD = null;
				    	}
				    	Application.CURRENT_PARTY_ID = null;
					}
				}
			}
		}
		if(cr!=null) {
			try {
				System.out.println(">>>>>>>>crclose");
				cr.close();
				Application.TRACE_RECEIVE_THREAD = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.print("Leaving");
	}
}
