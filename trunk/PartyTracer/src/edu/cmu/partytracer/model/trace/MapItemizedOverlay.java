package edu.cmu.partytracer.model.trace;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends ItemizedOverlay {
	private volatile ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	public MapItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public synchronized int size() {
		return mOverlays.size();
	}
	
	public synchronized void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}
	
	public synchronized void clear() {
		mOverlays.clear();
		populate();
	}
	
	@Override
	public synchronized boolean onTap(int index) {
		Log.e("xxxxx","xxxxxxxxxxxxxxxxxxxx");
		return true;
	}
	
	public void setMarker(int i, Drawable marker) {
		OverlayItem item = mOverlays.get(i);
	    item.setMarker(boundCenterBottom(marker));
	}
}
