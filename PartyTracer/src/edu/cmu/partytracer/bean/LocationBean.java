package edu.cmu.partytracer.bean;
/**
 * Location Bean that used by application (client sends to server)
 * @author km
 *
 */
public class LocationBean extends Bean{
	private static final long serialVersionUID = -1420647555425325410L;
	private Location location;
	
	public LocationBean(Location d) {
		super();
		location = d;
	}
	
	public Location getLocation() {
		return location;
	}
	
}
