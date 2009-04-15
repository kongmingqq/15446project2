package edu.cmu.partytracer.bean;
/**
 * Shared static info in the protocol
 * currently only type names in Vector<TYPE,BEAN>
 * and some protocol constant configurations
 * @author km
 *
 */
public final class Protocol {
	public static final String TYPE_LocationBean = "TRACE:LOC";
	public static final String TYPE_AggLocationBean = "TRACE:AGGLOC";
	public static final int EPOCH = 15000;
}
