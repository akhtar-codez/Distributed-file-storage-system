import java.io.*;
import java.util.List;

/*
 * StorageService
 * --------------
 * This class handles file storage and retrieval in the
 * distributed storage system.
 *
 * Features:
 * - Splits files into chunks
 * - Distributes chunks across storage nodes
 * - Uses MetadataManager to track chunk locations
 * - Reconstructs files during download
 */

public class StorageService {

    // Base storage directory
    private static final String STORAGE_PATH = "storage/";

    // Size of each chunk (1KB)
    private static final int CHUNK_SIZE = 1024;

    // Metadata manager instance
    MetadataManager metadataManager = new MetadataManager();


    /*
     * storeFile()
     * -----------
     * Reads the input file and splits it into chunks.
     * Each chunk is stored in a node selected by NodeManager.
     */

    public void storeFile(String filePath) {

        try {

            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            int chunkNumber = 1;

            while ((bytesRead = fis.read(buffer)) != -1) {

                // Select node dynamically
                String node = NodeManager.getNodeForChunk(chunkNumber);

                // Create chunk file name
                String chunkFileName = file.getName() + "_chunk_" + chunkNumber;

                // Store chunk
                FileOutputStream fos = new FileOutputStream(
                        STORAGE_PATH + node + "/" + chunkFileName);

                fos.write(buffer, 0, bytesRead);
                fos.close();

                // Update metadata
                metadataManager.addChunk(file.getName(), node);

                System.out.println("Stored " + chunkFileName + " in " + node);

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
     * Reconstructs the original file by retrieving
     * its chunks from storage nodes using metadata.
     */

    public void downloadFile(String fileName) throws IOException {

        String outputFile = "downloaded_" + fileName;

        FileOutputStream fos = new FileOutputStream(outputFile);

        // Get nodes where chunks are stored
        
        List<String> nodes = metadataManager.getChunks(fileName);

        int chunkNumber = 1;

        for (String node : nodes) {

            // Construct chunk path
            String chunkPath =
                    STORAGE_PATH + node + "/" + fileName + "_chunk_" + chunkNumber;

            File chunkFile = new File(chunkPath);

            if (!chunkFile.exists()) {

                System.out.println("Chunk missing from " + node);
                chunkNumber++;
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

            chunkNumber++;
        }

        fos.close();

        System.out.println("File reconstructed successfully as: " + outputFile);
    }
}