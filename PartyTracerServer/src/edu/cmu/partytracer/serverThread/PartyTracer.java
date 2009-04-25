package edu.cmu.partytracer.serverThread;


public class PartyTracer {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
			Thread serverTCPThread = null;
			ServerUDPThread.srThread serverUDPThread = null;
			ServerSingleton.getInstance().model.getCreateTableDAO().createTables();
			//set up port to listen
			serverTCPThread = new ServerTCPThread(ServerSingleton.serverPortNumber);
			System.out.println("TCP Thread Start!");
			serverTCPThread.start();
			serverUDPThread = new ServerUDPThread.srThread();
			System.out.println("UDP Thread Start!");
			serverUDPThread.start();
			
	}

}
