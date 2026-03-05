import java.io.IOException;

public class TestStorage {

    public static void main(String[] args) throws IOException {

        StorageService storage = new StorageService();

        // Upload a file
        storage.uploadFile("sample.txt");

        // Download file
        storage.downloadFile("sample.txt", "./");
    }
}