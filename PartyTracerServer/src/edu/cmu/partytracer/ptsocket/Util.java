package edu.cmu.partytracer.ptsocket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * Convert Object into Bytes and vice versa.
 * @author km
 *
 */
public class Util {
	public static byte[] objToBytes(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return baos.toByteArray();
	}
	
	public static Object bytesToObj(byte[] bytes) {
		Object obj = null;
		try {
			obj = new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return obj;
	}
}
