import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * NodeManager
 * -----------
 * Responsible for managing available storage nodes.
 *
 * Features:
 * - Automatically creates storage nodes
 * - Detects available nodes dynamically
 */

public class NodeManager {

    // Base storage directory
    private static final String STORAGE_PATH = "storage/";

    // Number of nodes in the system
    private static final int NODE_COUNT = 3;


    /*
     * Ensures storage nodes exist.
     * If not, they are created automatically.
     */
    public static void initializeNodes() {

        File storageDir = new File(STORAGE_PATH);

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        for (int i = 1; i <= NODE_COUNT; i++) {

            File nodeDir = new File(STORAGE_PATH + "node" + i);

            if (!nodeDir.exists()) {

                nodeDir.mkdir();
                System.out.println("Created node: node" + i);
            }
        }
    }


    /*
     * Returns list of available storage nodes
     */
    public static List<String> getAvailableNodes() {

        List<String> nodes = new ArrayList<>();

        File storageDir = new File(STORAGE_PATH);

        File[] files = storageDir.listFiles();

        if (files == null) return nodes;

        for (File file : files) {

            if (file.isDirectory()) {

                nodes.add(file.getName());
            }
        }

        return nodes;
    }

    /*
    * Simulates node failure by deleting the node directory.
    */
    public static void simulateNodeFailure(String nodeName) {

        File nodeDir = new File(STORAGE_PATH + nodeName);

        if (!nodeDir.exists()) {

            System.out.println("Node does not exist.");
            return;
        }

        File[] files = nodeDir.listFiles();

        // Delete all files inside the node
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        // Delete the node folder
        nodeDir.delete();

        System.out.println("Node failure simulated: " + nodeName);
    }
}