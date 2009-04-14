package edu.cmu.partytracer.activity.trace;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.cmu.partytracer.R;
import edu.cmu.partytracer.model.trace.MapItemizedOverlay;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

public class Map extends MapActivity {
	LinearLayout linearLayout;
	MapView mapView;
	ZoomControls mZoom;
	
	List<Overlay> mapOverlays;
	Drawable drawable;
	MapItemizedOverlay itemizedOverlay;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trace);
        
        linearLayout = (LinearLayout) findViewById(R.id.zoomview);
        mapView = (MapView) findViewById(R.id.mapview);
        mZoom = (ZoomControls) mapView.getZoomControls();
        
        linearLayout.addView(mZoom);

        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.marker);
        itemizedOverlay = new MapItemizedOverlay(drawable);
        
        GeoPoint point = new GeoPoint(40444314,-79942961);
        OverlayItem overlayitem = new OverlayItem(point, "", "");
        GeoPoint point2 = new GeoPoint(35410000, 139460000);
        OverlayItem overlayitem2 = new OverlayItem(point2, "", "");

        itemizedOverlay.addOverlay(overlayitem);
        itemizedOverlay.addOverlay(overlayitem2);
        mapOverlays.add(itemizedOverlay);
        
        MapController mc = mapView.getController();
        mc.setZoom(17);
        mc.setCenter(point);
        
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.v("end", "end loading");
		Thread t = new RunMe();
		t.start();
    }
    
    private class RunMe extends Thread {
	    public void run() {
	        for(int i=0;i<50000000;i+=100) {
	        mapOverlays.remove(itemizedOverlay);
	        itemizedOverlay = new MapItemizedOverlay(drawable);
	        
	        GeoPoint point = new GeoPoint(40444314+i,-79942961+i);
	        OverlayItem overlayitem = new OverlayItem(point, "", "");
	
	        itemizedOverlay.addOverlay(overlayitem);
	        mapOverlays.add(itemizedOverlay);
	        mapView.postInvalidate();
	        MapController mc = mapView.getController();
	        if(i%3000 == 0)	mc.animateTo(point);

	        Log.v("move", "move"+i);
	        try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        }
	    }
    }
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}