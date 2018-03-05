package actions;

import core.UserDataBase;
import messages.InfoMessage;
import messages.RegMessage;

import java.io.*;

public class RegistrationAction {
	private InputStream inputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	public RegistrationAction(InputStream inputStream, OutputStream outputStream){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	public void register(RegMessage regMessage) {
		String userName = regMessage.getUsername();
		String pwd = regMessage.getPwd();
		try {
			if (UserDataBase.registerUser(userName, pwd)) {
				objectOutputStream = new ObjectOutputStream(outputStream);
				objectOutputStream.writeObject(new InfoMessage("Successfully registered"));
			} else {
				objectOutputStream = new ObjectOutputStream(outputStream);
				objectOutputStream.writeObject(new InfoMessage("User already exists"));
			}
		} catch (IOException e){
			System.out.println("Smth went wrong O_o - register");
		}
	}
}
