package edu.cmu.partytracer.activity.trace;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.cmu.partytracer.R;
import edu.cmu.partytracer.bean.AggLocationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.model.trace.CacheQueue;
import edu.cmu.partytracer.model.trace.LocationMap;
import edu.cmu.partytracer.model.trace.LocationWithMove;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

public class Map extends MapActivity {
	//TODO this belongs to system
	static volatile CacheQueue cache = new CacheQueue();
	LinearLayout linearLayout;
	MapView mapView;
	ZoomControls mapZoom;
	
	volatile List<Overlay> mapOverlays;
	Drawable marker,marker_unknown,marker_destination,marker_me;
	volatile MapItemizedOverlay itemizedOverlay;
	volatile MapItemizedOverlay destinationOverlay;
	MyLocationOverlay myLocationOverlay;
	
	ProcessThread t;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.trace);
        
        linearLayout = (LinearLayout) findViewById(R.id.zoomview);
        mapView = (MapView) findViewById(R.id.mapview);
        //TODO extends ZoomControls to provide fit button and quit button(or catch key events)
        mapZoom = (ZoomControls) mapView.getZoomControls();
        linearLayout.addView(mapZoom);

        mapOverlays = mapView.getOverlays();
        marker = this.getResources().getDrawable(R.drawable.marker);
        marker_unknown = this.getResources().getDrawable(R.drawable.marker_unknown);
        marker_destination = this.getResources().getDrawable(R.drawable.marker_destination);
        marker_me = this.getResources().getDrawable(R.drawable.marker_me);
        Drawable info_waiting = this.getResources().getDrawable(R.drawable.info_waiting);
        
        
        //TODO read destination from invitation
        
        GeoPoint destinationPoint = new GeoPoint(40444314,-79942961);
        OverlayItem destination = new OverlayItem(destinationPoint, "Destination", "party info here");
        destinationOverlay = new MapItemizedOverlay(marker_destination);
        destinationOverlay.addOverlay(destination);
        mapOverlays.add(destinationOverlay);
        
        GeoPoint myPoint = new GeoPoint(40444314,-79942961);
        OverlayItem me = new OverlayItem(myPoint, "Destination", "Party info here");
        itemizedOverlay = new MapItemizedOverlay(marker_unknown);
        itemizedOverlay.addOverlay(me);
        itemizedOverlay.setMarker(0, info_waiting);
        mapOverlays.add(itemizedOverlay);
        
        //TODO my location info and my location update thread
        
        MapController mc = mapView.getController();
        mc.setZoom(13);
        mc.setCenter(destinationPoint);
        
        tempTest.test();       
        
        //Thread t = new MapProcessThread(mapView, mapOverlays, marker, itemizedOverlay);
        t = new ProcessThread();
		t.start();
		        
    }
    
    public void onStop() {
    	t.interrupt();
    	tempTest.stop();
    	super.onStop();
    }
        
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, 1, 0, "Zoom Fit").setIcon(R.drawable.menu_zoom);
	    menu.add(0, 2, 0, "Stop").setIcon(R.drawable.menu_stop);
	    menu.add(0, 3, 0, "Settings").setIcon(R.drawable.menu_settings);
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case 1:
	    	//do sth
	        return true;
	    case 2:
	    	//do sth
	        return true;
	    case 3:
	    	//do sth
	        return true;
	    }
	    return false;
	}
	
	
	
	
	
	
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
		public boolean onTap(int index) {
			synchronized(itemizedOverlay) {
				synchronized(mapView) {
				OverlayItem current = mOverlays.get(index);
				//bad for concurrent control
				//mapView.getController().animateTo(current.getPoint());
				Toast.makeText(getBaseContext(), current.getTitle()+"\nabc: xxxx\ndef: ook\nghi: "+index, Toast.LENGTH_SHORT).show();
				}
			}
			return true;
		}
		
		public synchronized void setMarker(int i, Drawable marker) {
			OverlayItem item = mOverlays.get(i);
		    item.setMarker(boundCenterBottom(marker));
		}
		
		@Override
		public void draw (Canvas canvas, MapView mapView, boolean shadow) {
			synchronized(mapView) {
				super.draw(canvas, mapView, false);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//*
	public class ProcessThread extends Thread{
		int TIME_GRAIN = 1000;
		int EPOCH = Protocol.EPOCH;
		
	
		public void run() {

			try {
				sleep(EPOCH);
			} catch (InterruptedException e) {
				interrupt();
			}
			
			Log.v("!!!!!!!","Run process location! ");
			//epoch loop
			//dequeue and 2 maps
			int i = 0;
			LocationMap curMap = null;
			LocationMap preMap = null;
			int retryAttemp = 0;
			//TODO end mark by system class, to end this thread
			while(true) {
				if(isInterrupted())	{
					Log.v("####","thread ends ProcessThread ");
					break;
				}
				if(i>5 && retryAttemp > 10) {
					
					Log.v("!!!!!!!","Leaving thread! ");
					break;
				}
				i++;
				AggLocationBean alb = cache.dequeue();
				if(alb != null) {
					retryAttemp = 0; 
					if(curMap==null) {
						curMap = new LocationMap(alb);
					} else {
						try {
							preMap = (LocationMap) curMap.copy();
							curMap.update(alb);
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.v("!!!!!!!","Dequeued AGGLOC packet! ");
				} else {
					Log.v("!!!!!!!","Waiting! "+i);
		    		try {
						sleep(1000);
					} catch (InterruptedException e) {
						interrupt();
					}
					retryAttemp++;
					i--;
					continue;
				}
				
				if(preMap == null) {
					//TODO Loading
					
					Log.v("!!!!!!!","initial waiting! "+i);
		    		try {
						sleep(1000);
					} catch (InterruptedException e) {
						interrupt();
					}
					i--;
					continue;
				}
				
				if(curMap!=null) {
					int countTo = EPOCH/TIME_GRAIN;
					//epoch/1000ms loop to animate	
					for(int count=0;count<countTo;count++) {
						synchronized(itemizedOverlay) {
							synchronized(mapOverlays) {
								synchronized(mapView) {
									mapOverlays.remove(itemizedOverlay);
								}
							}
							itemizedOverlay.clear();
							//itemizedOverlay = new MapItemizedOverlay(marker_unknown);
							
							//for every user
							for(String id:preMap.getIdSet()) {
								LocationWithMove preLoc = preMap.getLocationById(id);
								LocationWithMove curLoc = curMap.getLocationById(id);
								if(curLoc != null && count==0) {
									int preLat = preLoc.getLatitude();
									int preLng = preLoc.getLongitude();
									int curLat = curLoc.getLatitude();
									int curLng = curLoc.getLongitude();
							        Log.e("!!!!!", "preLat "+preLat+" curLat "+curLat);
									int latMove = (curLat-preLat)/countTo;
									int lngMove = (curLng-preLng)/countTo;
									preLoc.setLatMove(latMove);
									preLoc.setLngMove(lngMove);
							        Log.v("!!!!!", id+" LATMove: "+latMove+" LNGMove: "+lngMove);
								}
								//else if doesnt exist for curLoc, default 0 move applies
								
						        GeoPoint point = new GeoPoint(preLoc.getLatitude()+preLoc.getLatMove()*(count+1),preLoc.getLongitude()+preLoc.getLngMove()*(count+1));
						        OverlayItem overlayitem = new OverlayItem(point, preLoc.getId(), "");
	
								//synchronized(itemizedOverlay) {
							        itemizedOverlay.addOverlay(overlayitem);
							        if(preLoc.getLatMove()!=0||preLoc.getLngMove()!=0) {
							        	//overlayitem.setMarker(marker);
							        	itemizedOverlay.setMarker(itemizedOverlay.size()-1, marker);
							        }
								//}
						        //Log.v("!!!!!", "Overlay Item ID: "+id);
							}
					        //Log.v("!!!!!", "Overlay Item size: "+itemizedOverlay.size());
						
							synchronized(mapOverlays) {
								synchronized(mapView) {
									mapOverlays.add(itemizedOverlay);
									mapView.postInvalidate();
								}
							}
						}
						
				        //TODO some code to adjust scale
				        try {
							sleep(TIME_GRAIN);
						} catch (InterruptedException e) {
							interrupt();
						}
					}
				}
			}		
		}
	}//*/
}