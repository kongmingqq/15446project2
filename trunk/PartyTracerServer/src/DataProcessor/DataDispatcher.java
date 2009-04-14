package DataProcessor;

import java.util.Vector;

import DataBeans.InvitationBean;


public class DataDispatcher {
	public static void storeMsg(Vector<Object> input){
		//TODO: if needed, parse the input, otherwise, just transform it directly.
		String iID = (String)input.get(0);
		System.out.println(iID);
		InvitationBean myBean = (InvitationBean)input.get(1);
		System.out.println(myBean.getSampleData());
		System.out.println("Current System Time is:"+myBean.getTime());
	}
}
