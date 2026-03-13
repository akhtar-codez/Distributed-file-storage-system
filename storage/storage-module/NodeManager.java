import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * NodeManager
 * -----------
 * This class is responsible for managing storage nodes.
 * Instead of hardcoding node1, node2, node3, this class
 * automatically detects all available storage nodes
 * from the storage directory.
 *
 * This makes the system scalable because new nodes can
 * be added without changing the code.
 */

public class NodeManager {

    // Base storage directory
    private static final String STORAGE_PATH = "storage/";

    /*
     * getAvailableNodes()
     * -------------------
     * This method scans the storage directory and
     * returns a list of all node folders.
     *
     * Example directory structure:
     *
     * storage/
     *    node1
     *    node2
     *    node3
     *
     * Output:
     * [node1, node2, node3]
     */

    public static List<String> getAvailableNodes() {

        List<String> nodes = new ArrayList<>();

        File storageDir = new File(STORAGE_PATH);

        // List all directories inside storage/
        File[] files = storageDir.listFiles();

        if (files != null) {
            for (File file : files) {

                // Check if it is a directory (node)
                if (file.isDirectory()) {
                    nodes.add(file.getName());
                }
            }
        }

        return nodes;
    }

    /*
     * getNodeForChunk()
     * -----------------
     * This method decides which node should store
     * a particular chunk.
     *
     * We use round-robin distribution so that
     * chunks are evenly distributed across nodes.
     */

    public static String getNodeForChunk(int chunkNumber) {

        List<String> nodes = getAvailableNodes();

        if (nodes.isEmpty()) {
            throw new RuntimeException("No storage nodes available!");
        }

        // Round robin node selection
        return nodes.get((chunkNumber - 1) % nodes.size());
    }
}