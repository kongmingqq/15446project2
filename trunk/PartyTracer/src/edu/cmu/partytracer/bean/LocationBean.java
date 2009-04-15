package edu.cmu.partytracer.bean;

import java.io.Serializable;

/**
 * Location Bean that used by application (client sends to server)
 * @author km
 *
 */
public class LocationBean extends Bean implements Serializable {
	private static final long serialVersionUID = -1420647555425325410L;
	private Location location;
	private boolean sleepMode;
	
	public LocationBean(Location d, boolean m) {
		super();
		location = d;
		sleepMode = m;
	}
	
	public Location getLocation() {
		return location;
	}

	public boolean isSleepMode() {
		return sleepMode;
	}
	
}
