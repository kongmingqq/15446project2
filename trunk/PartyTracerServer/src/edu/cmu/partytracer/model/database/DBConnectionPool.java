package edu.cmu.partytracer.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 
 * This is the connection pool which the user can put the idle connection into
 * the connection pool, and get the connection from the pool when he wants to
 * get a new connection with database again, which would improve the efficiency
 * of the database connection.
 * 
 */
public class DBConnectionPool {
	private int checkedOut;
	private Vector<Object> freeConnections = new Vector<Object>();
	private int maxConn;
	private String jdbcURL;
	private static int num = 0;
	private static int numActive = 0;

	public DBConnectionPool(String jdbcURL, int normalConn, int maxConn) {
		this.jdbcURL = jdbcURL;
		this.maxConn = maxConn;
		if (num == 0) {
			for (int i = 0; i < normalConn; i++) {
				Connection c = newConnection();
				if (c != null) {
					freeConnections.addElement(c);
					num++;
				}
			}
		} 
	}

	// free the idle connection into the connection pool
	public synchronized void freeConnection(Connection con) {
		freeConnections.addElement(con);
		num++;
		checkedOut--;
		numActive--;
		notifyAll();
	}

	// get one available connection
	public synchronized Connection getConnection() {
		Connection con = null;

		if (freeConnections.size() > 0) {// still have available connection
			num--;

			con = (Connection) freeConnections.firstElement();
			freeConnections.removeElementAt(0);
			try {
				if (con.isClosed()) {
					con = getConnection();
				}
			} catch (SQLException e) {
				con = getConnection();
			}
		}

		else if (maxConn == 0 || checkedOut < maxConn) { // if available
			// connection is
			// smaller than max
			// or max has no
			// limit
			con = newConnection();
		}

		if (con != null) {
			checkedOut++;
		}

		numActive++;
		return con;

	}

	// get one connection with time constraint
	public synchronized Connection getConnection(long timeout) {
		long startTime = new Date().getTime();
		Connection con;
		while ((con = getConnection()) == null) {

			try {
				wait(timeout);
			} catch (InterruptedException e) {
			}

			if ((new Date().getTime() - startTime) >= timeout) {
				return null;
			}
		}
		return con;
	}

	// close all the connection
	public synchronized void release() {
		Enumeration<Object> allConnections = freeConnections.elements();
		while (allConnections.hasMoreElements()) {
			Connection con = (Connection) allConnections.nextElement();
			try {
				con.close();
				num--;
			} catch (SQLException e) {
			}
		}
		freeConnections.removeAllElements();
		numActive = 0;
	}

	// new a connection
	private Connection newConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(jdbcURL);
		} catch (SQLException e) {
			return null;
		}
		return con;
	}

	public int getnum() {
		return num;
	}

	public int getnumActive() {
		return numActive;
	}

}