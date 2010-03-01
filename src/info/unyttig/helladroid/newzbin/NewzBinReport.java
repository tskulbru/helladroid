package info.unyttig.helladroid.newzbin;

public class NewzBinReport {
	private int nzbId;
	private long nzbSize;
	private String nzbName;
	
	NewzBinReport(int nzbId, long nzbSize, String nzbName) {
		this.nzbId = nzbId;
		this.nzbSize = nzbSize;
		this.nzbName = nzbName;
	}

	public int getNzbId() { return nzbId; }
	public long getNzbSize() { return nzbSize; }
	public String getNzbName() { return nzbName; }
}
