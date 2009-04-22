package edu.cmu.partytracer.model.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


public class PartyTracerPool {
	private String jdbcURL;
	private int maxConnect = 100;
	private int normalConnect = 1;
	static private PartyTracerPool instance = null;
	Driver driver = null;
	DBConnectionPool pool = null;
	String password;

	static synchronized public PartyTracerPool getInstance() {
		if (instance == null) {
			instance = new PartyTracerPool();
		}
		return instance;
	}

	/**
	 * @param jdbcDriverName :
	 *            in our case: is com.mysql.jdbc.driver
	 * @param jdbcURL :
	 *            jdbc:mysql:///partytracer
	 */
	public PartyTracerPool(String jdbcDriverName, String jdbcURL) {
		this.jdbcURL = jdbcURL;
		loadDrivers(jdbcDriverName);
		createPool();
	}

	private PartyTracerPool() {
	}

	// load and register the driver
	private void loadDrivers(String driverName) {

		String driverClassName = driverName;
		try {
			driver = (Driver) Class.forName(driverClassName).newInstance();
			DriverManager.registerDriver(driver);
		} catch (Exception e) {
			System.out.println("Error in pool: "+e.getMessage());
		}
	}

	public void createPool() {
		if (pool == null)
			pool = new DBConnectionPool(jdbcURL, normalConnect, maxConnect);
		else
			return;
		if (pool != null) {
		} else {
		}
	}

	// get a available connection, if no connection, then we create a new
	// connection
	public Connection getConnection() {
		if (pool != null) {
			return pool.getConnection();
		}
		return null;
	}

	// get a connection with time constraint
	public Connection getConnection(long time) {
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	// free the connection to the connection pool
	public void freeConnection(Connection con) {
		if (pool != null) {
			pool.freeConnection(con);
		}
	}

	// return the idle connection number
	public int getnum() {
		return pool.getnum();
	}

	// return the active connection number
	public int getnumActive() {
		return pool.getnumActive();
	}

	// close all the connection, withdraw the driver registration
	public synchronized void release() {
		pool.release();
		try {
			DriverManager.deregisterDriver(driver);
		} catch (SQLException e) {
		}

	}

}
