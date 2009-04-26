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

import edu.cmu.partytracer.Application;
import edu.cmu.partytracer.Message;
import edu.cmu.partytracer.R;
import edu.cmu.partytracer.activity.invitation.testing.TestDataGenerator;
import edu.cmu.partytracer.bean.AggLocationBean;
import edu.cmu.partytracer.bean.BeanVector;
import edu.cmu.partytracer.bean.Location;
import edu.cmu.partytracer.bean.LocationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.bean.TerminationBean;
import edu.cmu.partytracer.model.trace.CacheQueue;
import edu.cmu.partytracer.model.trace.LocationMap;
import edu.cmu.partytracer.model.trace.LocationWithMove;
import edu.cmu.partytracer.ptsocket.PTSocket;
import edu.cmu.partytracer.ptsocket.UDPSocket;

import android.app.Activity;
import android.content.Intent;
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
	static CacheQueue CACHE = Application.TRACE_CACHE;
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

        Location d = Application.CURRENT_PARTY_DESTINATION;
        GeoPoint destinationPoint = new GeoPoint(d.getLatitude(),d.getLongitude());
        OverlayItem destination = new OverlayItem(destinationPoint, "Destination", "party info here");
        destinationOverlay = new MapItemizedOverlay(marker_destination);
        destinationOverlay.addOverlay(destination);
        mapOverlays.add(destinationOverlay);
        
        GeoPoint myPoint = new GeoPoint(40442334,-79945971);
        OverlayItem me = new OverlayItem(myPoint, "Me", "Myself");
        MapItemizedOverlay myLocOverlay = new MapItemizedOverlay(marker_me);
        myLocOverlay.addOverlay(me);
        myLocOverlay.setMarker(0, marker_me);
        mapOverlays.add(myLocOverlay);
        
        //TODO my location info and my location update thread
        //myLocationOverlay = new MyLocationOverlay(this, mapView);
        //mapOverlays.add(myLocationOverlay);
        
        OverlayItem info = new OverlayItem(destinationPoint, "Destination", d.getId());
        itemizedOverlay = new MapItemizedOverlay(marker_unknown);
        itemizedOverlay.addOverlay(info);
        itemizedOverlay.setMarker(0, info_waiting);
        mapOverlays.add(itemizedOverlay);
        
        
        MapController mc = mapView.getController();
        mc.setZoom(13);
        mc.setCenter(destinationPoint);
        
        Thread trt = new TraceReceiveThread();
        trt.start();
        Application.TRACE_RECEIVE_THREAD = trt;
    	Application.TRACE_SLEEP_MODE = false;
    	
        //Thread t = new MapProcessThread(mapView, mapOverlays, marker, itemizedOverlay);
        t = new ProcessThread();
		t.start();
		        
    }
    
    public void onStop() {
    	t.interrupt();
    	if(Application.TRACE_RECEIVE_THREAD!=null) {
	    	Application.TRACE_RECEIVE_THREAD.interrupt();
	    	Application.TRACE_RECEIVE_THREAD = null;
    	}
    	Application.TRACE_SLEEP_MODE = true;
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
	    	//terminate bean sent to server
	    	sendTerminationBean();
	    	//start terminate thread to listen to server terminate bean
	    	Thread trt = new TerminateReceiveThread();
	    	trt.start();
	    	Application.CURRENT_PARTY_ID = null; //once stop, cannot enter
	    	
	    	//message
			Intent i = new Intent(this, Message.class);
			i.putExtra("Message", "You request has been sent");
			startActivity(i);
			
			//stop
			finish();    	
	        return true;
	    case 3:
	    	//do sth
	        return true;
	    }
	    return false;
	}
	
	
	public void sendTerminationBean() {
		PTSocket pts = null;
		
		try {
			pts = new UDPSocket(Application.SERVER_IP, Protocol.SERVER_TRACE_RECEIVE_PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(pts!=null) {
			//TODO how to deal with multiple parties??
			TerminationBean tb = new TerminationBean(Application.MY_PHONE_ID,Application.CURRENT_PARTY_ID);
			try {
				pts.sendObject(BeanVector.wrapBean(tb));
				Log.v("####","Termination bean got sent");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		public synchronized boolean onTap(int index) {
			synchronized(mapView) {
				OverlayItem current = mOverlays.get(index);
				//bad for concurrent control
				//mapView.getController().animateTo(current.getPoint());
				Toast.makeText(getBaseContext(), current.getTitle()+"\n"+TestDataGenerator.lookUpContact(current.getTitle()), Toast.LENGTH_SHORT).show();
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
		int TIME_GRAIN = 1250;
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
				AggLocationBean alb = CACHE.dequeue();
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
						//itemizedOverlay.clear();
						//itemizedOverlay = new MapItemizedOverlay(marker_unknown);
						MapItemizedOverlay newItemizedOverlay = new MapItemizedOverlay(marker_unknown);
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
						        newItemizedOverlay.addOverlay(overlayitem);
						        if(preLoc.getLatMove()!=0||preLoc.getLngMove()!=0) {
						        	//overlayitem.setMarker(marker);
						        	newItemizedOverlay.setMarker(newItemizedOverlay.size()-1, marker);
						        }
							//}
					        //Log.v("!!!!!", "Overlay Item ID: "+id);
						}
				        //Log.v("!!!!!", "Overlay Item size: "+itemizedOverlay.size());
						
						//synchronized(itemizedOverlay) {
							synchronized(mapView) {
								synchronized(mapOverlays) {
							mapOverlays.remove(itemizedOverlay);
							itemizedOverlay = newItemizedOverlay;
							mapOverlays.add(itemizedOverlay);
							mapView.postInvalidate();
								}
							}
						//}
						
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