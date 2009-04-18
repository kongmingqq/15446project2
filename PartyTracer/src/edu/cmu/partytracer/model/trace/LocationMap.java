package edu.cmu.partytracer.model.trace;

import java.util.HashMap;
import java.util.Set;

import edu.cmu.partytracer.bean.AggLocationBean;
import edu.cmu.partytracer.bean.Location;
/**
 * This class uses a HashMap to store Locations. Location can be looked up by id. 
 * @author km
 *
 */

public class LocationMap implements Cloneable {
	private HashMap<String,LocationWithMove> map;

	public LocationMap(AggLocationBean alb) {
		map = new HashMap<String,LocationWithMove>();
		for(Location l:alb.getLocations()) {
			LocationWithMove lwm = new LocationWithMove(l.getId(),l.getLatitude(),l.getLongitude());
			map.put(l.getId(), lwm);
		}
	}
	
	/**
	 * constructor used to clone
	 * @param m
	 */
	protected LocationMap(HashMap m) {
		map = m;
	}
	
	/**
	 * Once a location is added, it can only be updated, not removable.
	 * This method updates these locations based on new AggLocationBean received.
	 * @param alb
	 */
	public synchronized void update(AggLocationBean alb) {
		for(Location l:alb.getLocations()) {
			LocationWithMove lwm = new LocationWithMove(l.getId(),l.getLatitude(),l.getLongitude());
			map.put(l.getId(), lwm);
		}
	}
	
	public synchronized LocationWithMove getLocationById(String id) {
		return map.get(id);
	}
	
	public synchronized Set<String> getIdSet() {
		return map.keySet();
	}
	
	/**
	 * clone and return a copy
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public synchronized LocationMap copy() throws CloneNotSupportedException {
		HashMap<String,LocationWithMove> newMap = new HashMap<String,LocationWithMove>();
		for(String s:map.keySet()) {
			LocationWithMove l = map.get(s);
			LocationWithMove lwm = new LocationWithMove(l.getId(),l.getLatitude(),l.getLongitude());
			newMap.put(l.getId(), lwm);
		}
		return new LocationMap(newMap);
	}
}