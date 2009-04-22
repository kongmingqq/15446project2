package edu.cmu.partytracer.network;

public class DummyCommunicator extends AbstractComm{

	public String getMyNumber() {
		return "";
	}

	public boolean send(int identifier, Object obj) {
		return true;
	}

	public void initNumber(String myNumber) {
	}

}
