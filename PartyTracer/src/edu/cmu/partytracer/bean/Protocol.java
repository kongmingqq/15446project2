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
	public static final String TYPE_InvitationBean = "MODEL:INVITATION";
	public static final String TYPE_VoteBean = "MODEL:VOTE";
	public static final String TYPE_Request = "MODEL:REQUEST";
	public static final int EPOCH = 5000;
}
