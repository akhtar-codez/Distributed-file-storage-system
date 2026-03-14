import java.io.*;
import java.util.List;

/*
 * StorageService
 * --------------
 * Handles file storage and retrieval in the distributed storage system.
 *
 * Features:
 * - Splits files into chunks
 * - Distributes chunks across storage nodes
 * - Replicates chunks for fault tolerance
 * - Uses MetadataManager to track chunk locations
 * - Reconstructs files during download
 */

public class StorageService {

    // Base storage directory
    private static final String STORAGE_PATH = "storage/";

    // Size of each chunk (1KB)
    private static final int CHUNK_SIZE = 1024;

    // Number of replicas for each chunk
    private static final int REPLICATION_FACTOR = 2;

    // Metadata manager instance
    MetadataManager metadataManager = new MetadataManager();

    /*
     * storeFile()
     * -----------
     * Reads the input file, splits it into chunks,
     * and stores those chunks across multiple nodes.
     */
    public void storeFile(String filePath) {

        try {

            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);

            // Get available storage nodes
            List<String> nodes = NodeManager.getAvailableNodes();

            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            int chunkNumber = 1;

            while ((bytesRead = fis.read(buffer)) != -1) {

                // Create chunk file name
                String chunkFileName = file.getName() + "_chunk_" + chunkNumber;

                /*
                 * Store chunk replicas across nodes
                 * Example:
                 * chunk1 -> node1, node2
                 * chunk2 -> node2, node3
                 */
                for (int r = 0; r < REPLICATION_FACTOR; r++) {

                    // Select node using round-robin distribution
                    String node = nodes.get((chunkNumber + r) % nodes.size());

                    // Create output stream to store chunk
                    FileOutputStream fos = new FileOutputStream(
                            STORAGE_PATH + node + "/" + chunkFileName);

                    // Write chunk data
                    fos.write(buffer, 0, bytesRead);
                    fos.close();

                    // Update metadata
                    metadataManager.addChunk(file.getName(), node);

                    System.out.println("Stored " + chunkFileName + " in " + node);
                }

                chunkNumber++;
            }

            fis.close();

            System.out.println("File stored successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
 * downloadFile()
 * --------------
 * Reconstructs the file by retrieving chunk replicas
 * from available nodes.
 *
 * If one node fails, the system automatically
 * tries the replica stored in another node.
 */

    public void downloadFile(String fileName) throws IOException {

        String outputFile = "downloaded_" + fileName;

        FileOutputStream fos = new FileOutputStream(outputFile);

        // Get nodes containing chunk replicas
        List<String> nodes = metadataManager.getChunks(fileName);

        int chunkNumber = 1;

        for (int i = 0; i < nodes.size(); i += REPLICATION_FACTOR) {

            boolean chunkRecovered = false;

            // Try each replica node
            for (int r = 0; r < REPLICATION_FACTOR; r++) {

                if (i + r >= nodes.size()) break;

                String node = nodes.get(i + r);

                String chunkPath =
                        STORAGE_PATH + node + "/" + fileName + "_chunk_" + chunkNumber;

                File chunkFile = new File(chunkPath);

                if (!chunkFile.exists()) {

                    System.out.println("Chunk_" + chunkNumber +
                            " missing in " + node + ", trying replica...");
                    continue;
                }

                FileInputStream fis = new FileInputStream(chunkFile);

                byte[] buffer = new byte[CHUNK_SIZE];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {

                    fos.write(buffer, 0, bytesRead);
                }

                fis.close();

                System.out.println("Retrieved chunk_" + chunkNumber + " from " + node);

                chunkRecovered = true;
                break;
            }

            if (!chunkRecovered) {

                System.out.println("ERROR: Chunk_" + chunkNumber + " lost!");
            }

            chunkNumber++;
        }

        fos.close();

        System.out.println("File reconstructed successfully as: " + outputFile);
    }

    /*
    * showMetadata()
    * --------------
    * Displays metadata of stored files.
    */
    public void showMetadata() {
        metadataManager.printMetadata();
    }
}