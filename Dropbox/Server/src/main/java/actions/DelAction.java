package actions;

import core.Client;
import core.UserDataBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DelAction {

	public void delFile(Client client, String fileName) {
		String userName = client.getUserName();
		try {
			Files.delete(Paths.get("received/" + userName + "/" + fileName));
			UserDataBase.deleteFileFromDB(userName, fileName);
			System.out.println(userName + " deleted file " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
