public class ThreadRunner {

	public static void main(String[] args) {
		ClientTCPThread cThread = new ClientTCPThread("128.237.249.235", 1544);
		cThread.start();
	}

}
