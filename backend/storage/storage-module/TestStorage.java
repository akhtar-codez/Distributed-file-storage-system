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

        NodeManager.initializeNodes();

        StorageService storage = new StorageService();
        StorageService storageService = new StorageService();

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\nDistributed File Storage System");
            System.out.println("1 Upload File");
            System.out.println("2 Download File");
            System.out.println("3 Show System Status");
            System.out.println("4 Debug Metadata");
            System.out.println("5 Simulate Node Failure");
            System.out.println("6 Exit");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();

            if (choice == 1) {

                System.out.print("Enter file path: ");
                String fileName = scanner.next();

                storage.storeFile(fileName);

            } else if (choice == 2) {

                System.out.print("Enter file name: ");
                String fileName = scanner.next();

                storage.downloadFile(fileName);

            } else if (choice == 3) {

                SystemMonitor.showSystemStatus();

            } else if (choice == 4) {

                storage.showMetadata();

            }else if (choice == 5) {

                System.out.print("Enter node to fail (node1/node2/node3): ");
                String node = scanner.next();

                NodeManager.simulateNodeFailure(node);

            }else if (choice == 6) {

                System.out.println("Exiting system...");
                break;

            } else {

                System.out.println("Invalid option");

            }
        }
        scanner.close();
    }
}