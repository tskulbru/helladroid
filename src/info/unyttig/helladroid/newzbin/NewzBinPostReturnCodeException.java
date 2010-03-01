package info.unyttig.helladroid.newzbin;

public class NewzBinPostReturnCodeException extends Exception {
	private static final long serialVersionUID = -1183730855310135792L;
	
	public NewzBinPostReturnCodeException(Exception e) {
		super(e);
	}
	public NewzBinPostReturnCodeException(String e) {
		super(e);
	}

}
