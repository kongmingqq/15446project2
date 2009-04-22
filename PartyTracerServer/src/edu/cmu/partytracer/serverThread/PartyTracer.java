package edu.cmu.partytracer.serverThread;
import edu.cmu.partytracer.model.database.Model;


public class PartyTracer {
	public static final int serverPortNumber=10004;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
			Thread serverThread = null;
			//set up port to listen
			serverThread = new ServerThread(serverPortNumber);
			System.out.println("Thread Start!");
			Model model = new Model("com.mysql.jdbc.Driver", "jdbc:mysql:///partytracer");
			model.getCreateTableDAO().createTables();
			serverThread.start();
	}

}
