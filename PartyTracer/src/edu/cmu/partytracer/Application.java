package edu.cmu.partytracer;

import edu.cmu.partytracer.bean.Location;
import edu.cmu.partytracer.model.trace.CacheQueue;

/**
 * Singleton class to store variables for the whole application.
 * @author KM
 *
 */
public final class Application {
	public static volatile String SERVER_IP = "127.0.0.1";//;"128.237.250.136";//
	
	public static volatile Thread TRACE_RECEIVE_THREAD = null;
	public static volatile Thread TRACE_SEND_THREAD = null;
	public static volatile CacheQueue TRACE_CACHE = new CacheQueue();
	public static volatile boolean TRACE_SLEEP_MODE = true;

	public static volatile String CURRENT_PARTY_ID = null; 
	public static volatile Location CURRENT_PARTY_DESTINATION = new Location("CMU", 40444314,-79942961);
	public static volatile String MY_PHONE_ID = "User A"; //Temporary
}
