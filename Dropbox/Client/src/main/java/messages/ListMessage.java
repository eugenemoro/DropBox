package messages;

import java.io.Serializable;
import java.util.ArrayList;

public class ListMessage extends Message {
	private ArrayList<String> listOfFiles;

	public ListMessage() {
		listOfFiles = new ArrayList<>();
	}

	public ArrayList<String> getListOfFiles() {
		return listOfFiles;
	}

	public void setListOfFiles(ArrayList<String> listOfFiles) {
		this.listOfFiles.addAll(listOfFiles);
	}
}
