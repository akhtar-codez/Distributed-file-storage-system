public class TestStorage {

    public static void main(String[] args) {

        StorageService storageService = new StorageService();

        // Change this path to any file on your computer
        String sourcePath = "C:\\Users\\DELL\\Downloads\\index.pdf";

        storageService.saveFile(sourcePath, "copied_test.txt");

        System.out.println("Test completed.");

    }
}