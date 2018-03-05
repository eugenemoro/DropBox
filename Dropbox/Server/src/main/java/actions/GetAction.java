package actions;

import core.Client;
import core.UserDataBase;
import messages.FileMessage;
import messages.GetMessage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetAction {
	private InputStream inputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	public GetAction(InputStream inputStream, OutputStream outputStream){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	public void sendFile(Client client, GetMessage getMessage) {
		String fileName = getMessage.getFileName();
		String userName = client.getUserName();
 		if (UserDataBase.getListOfFiles(userName).contains(fileName)) {
			try {
				objectOutputStream = new ObjectOutputStream(outputStream);
				FileMessage fm = new FileMessage(fileName, Files.readAllBytes(Paths.get("received/" + userName + "/" + fileName)));
				objectOutputStream.writeObject(fm);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
