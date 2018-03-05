
import messages.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class Core{
	private static ObjectInputStream objectInputStream;
	private static ObjectOutputStream objectOutputStream;
	private static Socket socket;

	Core() {
		try {
			socket = new Socket("localhost", 8888);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String authenticate(String userName, String pwd){
		String reply = "";
		sendMessage(new AuthMessage(userName, pwd));
		reply = stringFromInfoMessage();
		return reply;
	}

	public static String register(String userName, String pwd){
		String reply = "";
		sendMessage(new RegMessage(userName, pwd));
		reply = stringFromInfoMessage();
		return reply;
	}


	private static String stringFromInfoMessage() {
		String s = "";
		Object o = readObject();
		if (o instanceof InfoMessage) {
			s = ((InfoMessage) o).getMessage();
		}
		return s;
	}

	public static void uploadFile(File file) {
		try {
			sendMessage(new FileMessage(file.getName(), Files.readAllBytes(file.toPath())));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getListOfFiles() {
		ArrayList<String> listOfFiles = new ArrayList<>();
		sendMessage(new ListMessage());
		Object o = readObject();
		if (o instanceof ListMessage) {
			listOfFiles.addAll(((ListMessage) o).getListOfFiles());
		}
		return listOfFiles;
	}

	public static void downloadFile(String fileName, File file) {
		sendMessage(new GetMessage(fileName));
		Object o = readObject();
		try {
			if (o instanceof FileMessage) {
				Files.write(Paths.get(file.getPath()), ((FileMessage) o).getData(), StandardOpenOption.CREATE);
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public static void logOff() {
			sendMessage(new InfoMessage("logoff"));
	}

	public static void deleteFile(String fileName) {
			sendMessage(new DelMessage(fileName));
	}

	private static void sendMessage (Message message){
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(message);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private static Object readObject() {
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			return objectInputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
