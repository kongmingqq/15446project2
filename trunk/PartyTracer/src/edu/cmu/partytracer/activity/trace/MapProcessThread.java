package edu.cmu.partytracer.activity.trace;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.cmu.partytracer.bean.AggLocationBean;
import edu.cmu.partytracer.bean.Protocol;
import edu.cmu.partytracer.model.trace.*;
/**
 * Thread to read location info from CacheQueue provided by System singleton 
 * class, and update on MapView.
 * @author km
 *
 */
public class MapProcessThread extends Thread{
	static int EPOCH = Protocol.EPOCH;
	static int TIME_GRAIN = 500;
	MapView mapView;
	List<Overlay> mapOverlays;
	Drawable marker;
	MapItemizedOverlay itemizedOverlay;
	CacheQueue cache = Map.CACHE;
	//TODO cache is from system
	
	public MapProcessThread(MapView mv, List<Overlay> lo, Drawable d, MapItemizedOverlay mio) {
		mapView = mv;
		mapOverlays = lo;
		marker = d;
		itemizedOverlay = mio;
	}
	
	public void run() {

		try {
			sleep(EPOCH);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
				continue;
			}
			
			if(curMap!=null) {
				int countTo = EPOCH/TIME_GRAIN;
			
				//epoch/1000ms loop to animate	
				for(int count=0;count<countTo;count++) {
			        mapOverlays.remove(itemizedOverlay);
			        //itemizedOverlay = new MapItemizedOverlay(marker_unknown);
			        itemizedOverlay.clear();
			        
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
				        OverlayItem overlayitem = new OverlayItem(point, "", "");
				        itemizedOverlay.addOverlay(overlayitem);
				        if(preLoc.getLatMove()!=0||preLoc.getLngMove()!=0) {
				        	//overlayitem.setMarker(marker);
				        	itemizedOverlay.setMarker(itemizedOverlay.size()-1, marker);
				        }

				        //Log.v("!!!!!", "Overlay Item ID: "+id);
					}
			        Log.v("!!!!!", "Overlay Item size: "+itemizedOverlay.size());
			        mapOverlays.add(itemizedOverlay);
			        mapView.postInvalidate();
			        
			        //TODO some code to adjust scale
			        try {
						sleep(TIME_GRAIN);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}		
	}
}
