package edu.cmu.partytracer.model.trace;

import edu.cmu.partytracer.bean.Location;
/**
 * Subclass of Location to provide movement variables.
 * latMove and lngMove are calculated by (nextPoint - currentPoint)/timeOfMoves at runtime
 * @author km
 *
 */

public class LocationWithMove extends Location{
	int latMove = 0;
	int lngMove = 0;
	public LocationWithMove(String id, int lat, int lng) {
		super(id, lat, lng);
	}
	
	public int getLatMove() {
		return latMove;
	}

	public void setLatMove(int latMove) {
		this.latMove = latMove;
	}

	public int getLngMove() {
		return lngMove;
	}

	public void setLngMove(int lngMove) {
		this.lngMove = lngMove;
	}	
}
