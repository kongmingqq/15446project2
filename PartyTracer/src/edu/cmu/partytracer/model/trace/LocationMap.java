package edu.cmu.partytracer.model.trace;

import java.util.HashMap;
import java.util.Set;

import com.google.android.maps.GeoPoint;

import edu.cmu.partytracer.Application;
import edu.cmu.partytracer.bean.AggLocationBean;
import edu.cmu.partytracer.bean.Location;
/**
 * This class uses a HashMap to store Locations. Location can be looked up by id. 
 * @author km
 *
 */

public class LocationMap implements Cloneable {
	private HashMap<String,LocationWithMove> map;
	int maxLatitude;
	int minLatitude;
	int maxLongitude;
	int minLongitude;
	public LocationMap(AggLocationBean alb) {
		map = new HashMap<String,LocationWithMove>();
		int maxLat = Integer.MIN_VALUE;
		int minLat = Integer.MAX_VALUE;
		int maxLng = Integer.MIN_VALUE;
		int minLng = Integer.MAX_VALUE;
		for(Location l:alb.getLocations()) {
			if(l.getId().equals(Application.MY_PHONE_ID)) continue; //me is different
			LocationWithMove lwm = new LocationWithMove(l.getId(),l.getLatitude(),l.getLongitude());
			map.put(l.getId(), lwm);
			maxLat = Math.max(maxLat, l.getLatitude());
			minLat = Math.min(minLat, l.getLatitude());
			maxLng = Math.max(maxLng, l.getLongitude());
			minLng = Math.min(minLng, l.getLongitude());
		}
		maxLatitude = maxLat;
		minLatitude = minLat;
		maxLongitude = maxLng;
		minLongitude = minLng;
	}
	
	/**
	 * constructor used to clone
	 * @param m
	 */
	protected LocationMap(HashMap m, int maxLat, int minLat, int maxLng, int minLng) {
		map = m;
		maxLatitude = maxLat;
		minLatitude = minLat;
		maxLongitude = maxLng;
		minLongitude = minLng;
	}
	
	/**
	 * Once a location is added, it can only be updated, not removable.
	 * This method updates these locations based on new AggLocationBean received.
	 * @param alb
	 */
	public synchronized void update(AggLocationBean alb) {
		int maxLat = Integer.MIN_VALUE;
		int minLat = Integer.MAX_VALUE;
		int maxLng = Integer.MIN_VALUE;
		int minLng = Integer.MAX_VALUE;
		for(Location l:alb.getLocations()) {
			LocationWithMove lwm = new LocationWithMove(l.getId(),l.getLatitude(),l.getLongitude());
			map.put(l.getId(), lwm);
			maxLat = Math.max(maxLat, l.getLatitude());
			minLat = Math.min(minLat, l.getLatitude());
			maxLng = Math.max(maxLng, l.getLongitude());
			minLng = Math.min(minLng, l.getLongitude());
		}
		maxLatitude = maxLat;
		minLatitude = minLat;
		maxLongitude = maxLng;
		minLongitude = minLng;
	}
	
	public synchronized LocationWithMove getLocationById(String id) {
		return map.get(id);
	}
	
	public synchronized Set<String> getIdSet() {
		return map.keySet();
	}
	
	public synchronized GeoPoint getCenterPoint() {
		return new GeoPoint((maxLatitude+minLatitude)/2,(maxLongitude+minLongitude)/2);
	}
	
	public synchronized int getLatitudeSpan() {
		return maxLatitude-minLatitude;
	}
	
	public synchronized int getLongitudeSpan() {
		return maxLongitude-minLongitude;
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
		return new LocationMap(newMap,maxLatitude,minLatitude,maxLongitude,minLongitude);
	}
}