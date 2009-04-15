package edu.cmu.partytracer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Aggregated Locations Bean sent by server and received by application
 * @author km
 *
 */
public class AggLocationBean extends Bean implements Serializable {
	private static final long serialVersionUID = 8229402698712080479L;
	private List<Location> locations;
	private int invitationId;
	
	public AggLocationBean(int id, List<Location> d) {
		super();
		invitationId = id;
		locations = d;
	}

	public List<Location> getLocations() {
		return locations;
	}
	
	public int getInvitationId() {
		return invitationId;
	}
}
