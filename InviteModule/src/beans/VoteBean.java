package beans;

public class VoteBean {
	public int getWhichInvite() {
		return whichInvite;
	}
	public void setWhichInvite(int whichInvite) {
		this.whichInvite = whichInvite;
	}
	
	public int[] getVoter() {
		return voters;
	}

	public void setVoter(int[] voters) {
		this.voters = voters;
	}
	
	private int whichInvite;
	private int[] voters;
	
	public VoteBean(){};
}
