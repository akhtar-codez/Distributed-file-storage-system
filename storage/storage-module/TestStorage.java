import java.io.IOException;

public class TestStorage {

    public static void main(String[] args) throws IOException {

        StorageService storage = new StorageService();

        // Upload file
        storage.storeFile("sample.txt");

        // Download file (reconstruct)
        storage.downloadFile("sample.txt");
    }
}