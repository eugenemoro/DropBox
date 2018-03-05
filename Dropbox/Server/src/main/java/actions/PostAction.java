package actions;

import core.Client;
import core.UserDataBase;
import messages.FileMessage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PostAction {

	public void getFile(Client client, FileMessage fileMessage) {
		String userName = client.getUserName();
		try {
			Files.write(Paths.get("received/" + userName + "/" + fileMessage.getName()), fileMessage.getData(), StandardOpenOption.CREATE);
			if (!UserDataBase.getListOfFiles(userName).contains(fileMessage.getName())){
				UserDataBase.addFileToDB(userName, fileMessage.getName());
			}
			System.out.println(userName + " uploaded file " + fileMessage.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
