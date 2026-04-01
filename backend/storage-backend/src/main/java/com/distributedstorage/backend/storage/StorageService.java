package com.distributedstorage.backend.storage;

import java.io.*;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

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
@Service
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

                String chunkFileName = file.getName() + "_chunk_" + chunkNumber;

                for (int r = 0; r < REPLICATION_FACTOR; r++) {

                    String node = nodes.get((chunkNumber + r) % nodes.size());

                    FileOutputStream fos = new FileOutputStream(
                            STORAGE_PATH + node + "/" + chunkFileName);

                    fos.write(buffer, 0, bytesRead);
                    fos.close();

                    metadataManager.addChunk(file.getName(), chunkNumber, node);

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
     * Reconstructs file using chunk metadata.
     * If a node fails, replica nodes are tried.
     */
    public void downloadFile(String fileName) {

        try {

            String outputFile = "downloaded_" + fileName;

            FileOutputStream fos = new FileOutputStream(outputFile);

            // Get all chunks of this file
            Set<Integer> chunks = metadataManager.getChunks(fileName);

            for (int chunkNumber : chunks) {

                List<String> nodes = metadataManager.getChunkNodes(fileName, chunkNumber);

                boolean found = false;

                for (String node : nodes) {

                    String chunkPath =
                            STORAGE_PATH + node + "/" + fileName + "_chunk_" + chunkNumber;

                    File chunkFile = new File(chunkPath);

                    if (!chunkFile.exists()) {

                        System.out.println("Chunk_" + chunkNumber + " missing in " + node + ", trying replica...");
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

                    found = true;
                    break;
                }

                if (!found) {

                    System.out.println("ERROR: Chunk_" + chunkNumber + " lost!");
                }
            }

            fos.close();

            System.out.println("File reconstructed successfully as: " + outputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
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