package edu.cmu.partytracer.activity.trace;

import edu.cmu.partytracer.Application;
import edu.cmu.partytracer.bean.BeanVector;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.ptsocket.PTSocket;
import edu.cmu.partytracer.ptsocket.UDPSocket;

public class TerminateReceiveThread extends Thread {
	private static final int TIMEOUT = Protocol.TERMINATION_TIMEOUT;
	
	public void run() {
		PTSocket tr = null;
		try {
			tr = new UDPSocket(Protocol.CLIENT_TERMINATE_RECEIVE_PORT);
			((UDPSocket)tr).setTimeout(TIMEOUT);
			while(tr!=null) {
				Object o = tr.receiveObject();
				BeanVector bv = new BeanVector(o);
				if(bv.getType().equals(Protocol.TYPE_TerminationBean)) {
					//remove
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//clean up, either timeout or has received TerminationBean from server
			if(Application.CURRENT_PARTY_ID==null) { //so it is safe to terminate
		    	if(Application.TRACE_SEND_THREAD!=null) {
			    	Application.TRACE_SEND_THREAD.interrupt();
			    	Application.TRACE_SEND_THREAD = null;
		    	}
			}
				
			//TODO how about multiple parties?
		}
	}
}
