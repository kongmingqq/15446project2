package edu.cmu.partytracer.activity.trace;

import android.util.Log;
import edu.cmu.partytracer.Application;
import edu.cmu.partytracer.bean.BeanVector;
import edu.cmu.partytracer.bean.Location;
import edu.cmu.partytracer.bean.LocationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.model.trace.CacheQueue;
import edu.cmu.partytracer.ptsocket.PTSocket;
import edu.cmu.partytracer.ptsocket.UDPSocket;

public class TraceSendThread extends Thread {
	static int EPOCH = Protocol.EPOCH;
	static CacheQueue CACHE = Application.TRACE_CACHE;
	static String SERVERIP = Application.SERVER_IP;
	static int SERVERPORT = Protocol.SERVER_TRACE_RECEIVE_PORT;
	static String MYPHONEID = Application.MY_PHONE_ID;
	static String PARTY_ID = Application.CURRENT_PARTY_ID;
	
	public void run() {
		PTSocket cs = null;
		
		try {
			cs = new UDPSocket(SERVERIP, SERVERPORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(cs!=null) {
			if(isInterrupted())	{
				Log.v("####","TraceSendThread ends");
				break;
			}
			//TODO how to deal with multiple parties??
			//TODO get real my location
			LocationBean lb = new LocationBean(new Location(MYPHONEID,40444334,-79942971),Application.TRACE_SLEEP_MODE, PARTY_ID);
			try {
				cs.sendObject(BeanVector.wrapBean(lb));
				Log.v("####","Location bean got sent");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				sleep(EPOCH);
			} catch (InterruptedException e) {
				interrupt();
			}
		}
        
	}
}
