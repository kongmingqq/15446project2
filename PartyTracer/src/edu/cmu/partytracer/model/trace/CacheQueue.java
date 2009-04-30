package edu.cmu.partytracer.model.trace;

import edu.cmu.partytracer.bean.AggLocationBean;
/**
 * Cache at most two AggLocationBean objects based on timestamp.
 * Prefer latest location info, a delayed packet will be dropped.
 * @author km
 *
 */

public class CacheQueue {
	private volatile AggLocationBean current = null;
	private volatile AggLocationBean next = null;
	
	public synchronized void enqueue(AggLocationBean alb) {
		if(next!=null) {
			//prefer latest location info
			if(next.getTime()>alb.getTime()) {
				if(current==null || current.getTime()<alb.getTime()) {
					current = alb;
					return;
				} else {
					return;
				}
			}			
			current = next;
		}
		next = alb;
	}
	public synchronized AggLocationBean dequeue() {
		AggLocationBean rv = current;
		current = next;
		next = null;
		return rv;
	}
	public synchronized void clear() {
		current = null;
		next = null;
	}
}
