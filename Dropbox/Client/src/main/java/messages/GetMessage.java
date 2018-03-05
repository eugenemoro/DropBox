package messages;

import java.io.Serializable;

public class GetMessage extends Message {
	private String fileName;

	public GetMessage(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
