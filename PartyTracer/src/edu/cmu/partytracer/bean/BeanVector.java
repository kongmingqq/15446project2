package edu.cmu.partytracer.bean;

import java.util.Vector;
/**
 * Bean Vector class to help implement using vector to wrap beans
 * @author km
 *
 */
public final class BeanVector {
	Vector<Object> v;
	
	@SuppressWarnings("unchecked")
	public BeanVector(Object o) throws BeanVectorException {
		if(o instanceof Vector) {
			v = (Vector<Object>) o;
		} else {
			throw new BeanVectorException("Not valid vector");
		}
	}
	
	public Object wrapBean(Bean b) {
		Vector<Object> v = new Vector<Object>();
		if(b instanceof LocationBean) { //only for the application
			v.add("TRACE");
		} else if(b instanceof AggLocationBean) { //only for the server
			v.add("TRACE");
		} else {
			return null;
		}
		v.add(b);
		return v;
	}
	
	public String getType() {
		return (String)(v.get(0));
	}
	
	public Bean getBean() {
		return (Bean)(v.get(1));
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
