import java.io.IOException;
import java.util.Scanner;

/*
 * TestStorage
 * -----------
 * This class is used to test the Distributed File Storage System.
 * It allows the user to upload and download any file dynamically
 * instead of using a hardcoded filename.
 */

public class TestStorage {

    public static void main(String[] args) throws IOException {

        StorageService storage = new StorageService();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Distributed File Storage System");
        System.out.println("1. Upload File");
        System.out.println("2. Download File");
        System.out.print("Choose option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        if (choice == 1) {

            storage.storeFile(fileName);

        } else if (choice == 2) {

            storage.downloadFile(fileName);

        } else {

            System.out.println("Invalid option");

        }

        scanner.close();
    }
}