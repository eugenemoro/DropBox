package messages;

public class FileMessage extends Message {
    private String name;
    private byte[] data;

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }
}
