package edu.cmu.partytracer.ptsocket;
/**
 * Party Tracer socket interface to encapsulate the transmission protocol (UDP/TCP)
 * only supports small packets
 * @author km
 *
 */
public interface PTSocket {
	public void sendObject(Object obj) throws Exception;
	public Object receiveObject() throws Exception;
	public void close() throws Exception;
}
