package edu.cmu.partytracer.model.database;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTableDAO {
	private PartyTracerPool connPool;

	public CreateTableDAO(PartyTracerPool connPool) {
		this.connPool = connPool;
	}

	/**
	 * @return true if it create tables successfully, or false if it fails.
	 */
	public boolean createTables() {
		try {

			Connection con = connPool.getConnection();
			DatabaseMetaData dbm = con.getMetaData();
			// check whether the table echo_case exist or not
			ResultSet rsCase = dbm.getTables(null, null, "PARTYTRACER", null);
			// if table echo_case does not exist, then create the table
			// echo_case
			if (!rsCase.next()) {

				Statement stmt = con.createStatement();
				String Query = "CREATE TABLE PARTYTRACER(" + "PARTY_ID VARCHAR(500) PRIMARY KEY, PARTY_SENDER VARCHAR(500), PARTY_TIMEOUT VARCHAR(500), PARTY_RESULT VARCHAR(500))";
				stmt.execute(Query);
				stmt.close();
			}

			ResultSet rsSite = dbm.getTables(null, null, "INVITELIST", null);
			// create a new table if it does not exist
			if (!rsSite.next()) {
				Statement stmt = con.createStatement();
				String Query = "CREATE TABLE INVITELIST(" + " INVITE_ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY," + "INVITE_PERSON VARCHAR(500) NOT NULL," + "PARTY_ID VARCHAR(500),"
						+ "FOREIGN KEY (PARTY_ID) REFERENCES PARTYTRACER(PARTY_ID) ON DELETE SET NULL) ";
				stmt.execute(Query);
				stmt.close();
			}
			
			// whether the job table exist or not
			ResultSet rsJob = dbm.getTables(null, null, "OPTIONLIST", null);
			if (!rsJob.next()) {
				Statement stmt = con.createStatement();
				String Query = "CREATE TABLE OPTIONLIST (" + "OPTION_ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY," + "OPTION_CONTENT VARCHAR(500)," + "PARTY_ID VARCHAR(500),"  
						 + "FOREIGN KEY (PARTY_ID) REFERENCES PARTYTRACER(PARTY_ID) ON DELETE SET NULL)";
				stmt.execute(Query);
				stmt.close();
			}

			connPool.freeConnection(con);
			return true;
		} catch (SQLException ex) {
			System.out.println("SQL Exception during creating tables: "+ex.getMessage());
			return false;
		} catch (Exception e) {
			System.out.println("Exception during creating tables: "+e.getMessage());
			return false;
		}
	}

}
