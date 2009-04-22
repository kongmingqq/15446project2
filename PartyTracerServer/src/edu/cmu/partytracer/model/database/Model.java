package edu.cmu.partytracer.model.database;

public class Model {
	private InvitationDAO invitationDAO;
	private CreateTableDAO createTableDAO;
	private PartyTracerPool connPool;

	public Model(String jdbcDriver, String jdbcURL){
		try {
			connPool = new PartyTracerPool(jdbcDriver, jdbcURL);
			invitationDAO = new InvitationDAO(connPool);
			createTableDAO = new CreateTableDAO(connPool);
		} catch (Exception e) {
			System.out.println("Error in Model: "+e.getMessage());
		}
	}

	public InvitationDAO getInvitationDAO() {
		return invitationDAO;
	}
	
	public CreateTableDAO getCreateTableDAO() {
		return createTableDAO;
	}
}
