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
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO PARTYTRACER(PARTY_ID,PARTY_SENDER, PARTY_TIMEOUT) VALUES(?,?,?)");
			pstmt.setString(1, partyID);
			pstmt.setString(2, invitationBean.getSender());
			pstmt.setString(3, invitationBean.getTimeout() + "");
			pstmt.addBatch();

//			for (String eachOption : invitationBean.getOptions()) {
//				pstmt = con.prepareStatement("INSERT INTO OPTIONLST(OPTION_CONTENT, PARTY_ID) VALUES(?,?)");
//				pstmt.setString(1, eachOption);
//				pstmt.setString(2, partyID);
//				pstmt.addBatch();
//			}
//
//			for (String eachPerson : invitationBean.getInviteList()){
//				pstmt = con.prepareStatement("INSERT INTO INVITATIONLIST(INVITE_PERSON, PARTY_ID) VALUES(?,?)");
//				pstmt.setString(1, eachPerson);
//				pstmt.setString(2, partyID);
//				pstmt.addBatch();
//			}
			
			pstmt.executeBatch();
			pstmt.close();
			connPool.freeConnection(con);
			return partyID;
		} catch (Exception e) {
			System.out.println("Store invitation information error: " + e.getMessage());
			return null;
		}
	}

}