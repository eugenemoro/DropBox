package messages;

public class InfoMessage extends Message {
	private String message;

	public InfoMessage(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
