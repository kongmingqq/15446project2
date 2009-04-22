package edu.cmu.partytracer.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

import edu.cmu.partytracer.bean.InvitationBean;

public class InvitationDAO {
	private PartyTracerPool connPool;

	public InvitationDAO(PartyTracerPool connPool) {
		this.connPool = connPool;
	}

	public boolean storeInvitationData(InvitationBean invitationBean) {
		try {
			Connection con = connPool.getConnection();
			String prepareString = "INSERT INTO PARTYTRACER(PARTY_ID,PARTY_SENDER, PARTY_TIMEOUT) VALUES(?,?,?)";
			PreparedStatement pstmt = con.prepareStatement(prepareString);
			pstmt.setString(1, System.currentTimeMillis()+"");
			pstmt.setString(2, invitationBean.getSender());
			pstmt.setString(3, invitationBean.getTimeout()+"");
			pstmt.execute();
			pstmt.close();
			connPool.freeConnection(con);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}