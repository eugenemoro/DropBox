package actions;

import messages.*;
import core.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

public class AuthenticationAction {
	private InputStream inputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	public AuthenticationAction(InputStream inputStream, OutputStream outputStream){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	public Client authenticate(AuthMessage authMessage){
		Client client = null;
		try {
			objectOutputStream = new ObjectOutputStream(outputStream);
			String[] userInput =  authMessage.getInfo();
			String userInfoFromDB = UserDataBase.getUserInfo(userInput[0]);
			String pwd = DigestUtils.sha256Hex(userInput[1]);
			if (!userInfoFromDB.equals("-1") && userInfoFromDB.equals(pwd)){
				client = new Client(userInput[0]);
				System.out.println(client.getUserName() + " authenticated");
				objectOutputStream.writeObject(new InfoMessage("Successfully connected"));
			}
			else {
				objectOutputStream.writeObject(new InfoMessage("Wrong login/password"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return client;
	}
}
