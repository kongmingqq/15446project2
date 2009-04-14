package edu.cmu.partytracer.dataProcessor;

import java.util.Vector;

import edu.cmu.partytracer.bean.InvitationBean;



public class DataDispatcher {
	public static void storeMsg(Vector<Object> input){
		//TODO: if needed, parse the input, otherwise, just transform it directly.
		String iID = (String)input.get(0);
		System.out.println(iID);
		InvitationBean myBean = (InvitationBean)input.get(1);
		System.out.println(myBean.getId());
		System.out.println("Current System Time is:"+myBean.getTimeout());
	}
}
