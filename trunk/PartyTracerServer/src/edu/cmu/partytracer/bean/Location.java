package edu.cmu.partytracer.bean;

import java.io.Serializable;

/**
 * Location data structure
 * @author km
 *
 */
public class Location implements Serializable {
	private static final long serialVersionUID = 1920709317746244839L;
	private String id; //sender id, i.e. phone number
	private int latitude;
	private int longitude;
	public Location(String id, int lat, int lng) {
		this.id = id;
		latitude = lat;
		longitude = lng;
	}
	public String getId() {
		return id;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getLongitude() {
		return longitude;
	}
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	public String toString() {
		return "ID: "+id+"; LAT: "+latitude+"; LNG: "+longitude;
	}
}
