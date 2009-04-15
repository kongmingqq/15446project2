package edu.cmu.partytracer.bean;

import java.util.List;

/**
 * Aggregated Locations Bean sent by server and received by application
 * @author km
 *
 */
public class AggLocationBean extends Bean {
	private static final long serialVersionUID = 8229402698712080479L;
	private List<Location> locations;
	
	public AggLocationBean(List<Location> d) {
		super();
		locations = d;
	}

	public List<Location> getLocations() {
		return locations;
	}
}
