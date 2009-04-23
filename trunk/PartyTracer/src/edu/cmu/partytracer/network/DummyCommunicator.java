package edu.cmu.partytracer.network;

public class DummyCommunicator extends AbstractComm{

	public String getMyNumber() {
		return "";
	}

	public void send(String identifier, Object obj) {
	}

	public void initNumber(String myNumber) {
	}

}
