package core;

import actions.*;
import messages.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ClientThread implements Runnable {
	public Socket socket;
	private Client client;

	public ClientThread(Socket socket){
		this.socket = socket;
	}

	@Override
	public void run () {
		InputStream inputStream;
		OutputStream outputStream;
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream;
			AuthenticationAction authAction = new AuthenticationAction(inputStream, outputStream);
			RegistrationAction regAction = new RegistrationAction(inputStream, outputStream);
			PostAction postAction = new PostAction();
			GetAction getAction = new GetAction(inputStream, outputStream);
			DelAction delAction = new DelAction();

			while (socket.isConnected()){
				inputStream = socket.getInputStream();
				Object request = null;
				try {
					ObjectInputStream inputObjectStream = new ObjectInputStream(inputStream);
					request = inputObjectStream.readObject();
				} catch (EOFException e){}
				if (request instanceof RegMessage){
					RegMessage regMessage = (RegMessage) request;
					regAction.register(regMessage);
				} else if (request instanceof AuthMessage){
					AuthMessage authMessage = (AuthMessage) request;
					client = authAction.authenticate(authMessage);
				}	else if (request instanceof ListMessage){
					objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					ListMessage listMessage = new ListMessage();
					listMessage.setListOfFiles(UserDataBase.getListOfFiles(client.getUserName()));
					objectOutputStream.writeObject(listMessage);
				} else if (request instanceof FileMessage){
					FileMessage fileMessage = (FileMessage) request;
					if (!client.equals(null)){
						postAction.getFile(client, fileMessage);
					}
				} else if (request instanceof GetMessage){
					GetMessage getMessage = (GetMessage) request;
					if (!client.equals(null)){
						getAction.sendFile(client, getMessage);
					}
				} else if (request instanceof DelMessage){
					DelMessage delMessage = (DelMessage) request;
					if (!client.equals(null)){
						delAction.delFile(client, delMessage.getFileName());
					}
				} else if (request instanceof InfoMessage) {
					InfoMessage infoMessage = (InfoMessage) request;
					if (infoMessage.getMessage().equals("logoff")) {
						System.out.println(client.getUserName() + " logged off");
						client = null;
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
