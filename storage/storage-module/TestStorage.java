public class TestStorage {

    public static void main(String[] args) {

        StorageService storageService = new StorageService();

        storageService.initializeStorage();

        storageService.saveFileInChunks(
        "hello.txt",
        "Hello Distributed Storage System! This project simulates Google Drive storage."
        );

        String result = storageService.readFile("hello.txt");

        System.out.println("Reconstructed File:");
        System.out.println(result);

    }

}