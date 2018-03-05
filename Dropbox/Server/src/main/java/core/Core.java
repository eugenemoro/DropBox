package core;

import core.ClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Core {
	Core() {
		try {
			ServerSocket serverSocket = new ServerSocket(8888);
			while (true){
				Socket socket = serverSocket.accept();
				System.out.println("client connected");
				new Thread(new ClientThread(socket)).start();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
