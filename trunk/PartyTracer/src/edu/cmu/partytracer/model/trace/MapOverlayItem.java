package edu.cmu.partytracer.model.trace;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MapOverlayItem extends OverlayItem {
	private String id;
	
	public MapOverlayItem(GeoPoint point, String title, String snippet, String id) {
		super(point, title, snippet);
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
