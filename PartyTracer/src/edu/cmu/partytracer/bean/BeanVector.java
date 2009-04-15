package edu.cmu.partytracer.bean;

import java.util.Vector;
/**
 * Bean Vector class to help implement using vector to wrap beans
 * @author km
 *
 */
public final class BeanVector {
	private Vector<Object> v;
	
	@SuppressWarnings("unchecked")
	public BeanVector(Object o) throws BeanVectorException {
		if(o instanceof Vector) {
			v = (Vector<Object>) o;
		} else {
			throw new BeanVectorException("Not valid vector");
		}
	}
	
	public String getType() {
		return (String)(v.get(0));
	}
	
	public Bean getBean() {
		return (Bean)(v.get(1));
	}
	
	public static Object wrapBean(Bean b) {
		Vector<Object> vo = new Vector<Object>();
		if(b instanceof LocationBean) { //only for the application
			vo.add(Protocol.TYPE_LocationBean);
		} else if(b instanceof AggLocationBean) { //only for the server
			vo.add(Protocol.TYPE_AggLocationBean);
		} else {
			return null;
		}
		vo.add(b);
		return vo;
	}

	private class BeanVectorException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4543631841056893530L;

		public BeanVectorException(String s) {
			super(s);
		}
	}
}
