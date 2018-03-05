package messages;

public class DelMessage extends Message {
	private String fileName;

	public DelMessage(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
