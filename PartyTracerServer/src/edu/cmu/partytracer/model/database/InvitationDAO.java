package edu.cmu.partytracer.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

import edu.cmu.partytracer.bean.InvitationBean;

public class InvitationDAO {
	private PartyTracerPool connPool;

	public InvitationDAO(PartyTracerPool connPool) {
		this.connPool = connPool;
	}

	public String storeInvitationData(InvitationBean invitationBean) {
		try {
			Connection con = connPool.getConnection();
			String partyID = System.currentTimeMillis() + "";
			PreparedStatement pstmt1 = con.prepareStatement("INSERT INTO PARTYTRACER(PARTY_ID,PARTY_SENDER, PARTY_TIMEOUT) VALUES(?,?,?)");
			PreparedStatement pstmt2 = con.prepareStatement("INSERT INTO OPTIONLIST(OPTION_CONTENT, PARTY_ID, VOTE_CNT) VALUES(?,?,?)");
			PreparedStatement pstmt3 = con.prepareStatement("INSERT INTO INVITELIST(INVITE_PERSON, PARTY_ID) VALUES(?,?)");
			pstmt1.setString(1, partyID);
			pstmt1.setString(2, invitationBean.getSender());
			pstmt1.setString(3, invitationBean.getTimeout() + "");
			pstmt1.execute();
			pstmt1.close();

			for (String eachOption : invitationBean.getOptions()) {
				pstmt2.setString(1, eachOption);
				pstmt2.setString(2, partyID);
				pstmt2.setInt(3, 0);
				pstmt2.addBatch();
			}
			pstmt2.executeBatch();
			pstmt2.close();

			for (String eachPerson : invitationBean.getInviteList()){
				pstmt3.setString(1, eachPerson);
				pstmt3.setString(2, partyID);
				pstmt3.addBatch();
			}
			pstmt3.executeBatch();
			pstmt3.close();
			connPool.freeConnection(con);
			return partyID;
		} catch (Exception e) {
			System.out.println("Store invitation information error: " + e.getMessage());
			return null;
		}
	}

}