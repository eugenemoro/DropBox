package messages;

public class AuthMessage extends Message {
	private String[] info;

	public AuthMessage(String login, String pwd){
		this.info = new String[]{login, pwd};
	}

	public String[] getInfo() {
		return info;
	}
}
