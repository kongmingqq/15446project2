package edu.cmu.partytracer.network;

public class DummyCommunicator extends AbstractComm{

	public String getMyNumber() {
		return "";
	}

	public boolean send(String identifier, Object obj) {
		return true;
	}

	public void initNumber(String myNumber) {
	}

}
