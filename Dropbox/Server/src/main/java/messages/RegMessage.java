package messages;

public class RegMessage extends Message {
	private String username;
	private String pwd;

	public RegMessage(String username, String pwd){
		this.username = username;
		this.pwd = pwd;
	}

	public String getUsername() {
		return username;
	}

	public String getPwd() {
		return pwd;
	}
}
