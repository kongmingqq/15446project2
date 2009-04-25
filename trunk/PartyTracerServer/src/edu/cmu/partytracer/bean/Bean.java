package edu.cmu.partytracer.bean;

import java.util.Date;

/**
 * Abstract bean provides timestamp, and comparable interface to sort beans
 * @author km
 *
 */
public abstract class Bean implements Comparable<Bean> {
	protected long time;
	
	public Bean() {
		time = (new Date()).getTime(); //current timestamp		
	}
	
	public int compareTo(Bean o) {
		if(time-o.getTime()>0) return 1;
		else if(time-o.getTime()<0) return -1;
		return 0;
	}

	public long getTime() {
		return time;
	}
}
