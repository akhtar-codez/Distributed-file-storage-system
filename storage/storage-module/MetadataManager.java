import java.io.*;
import java.util.*;

/*
 * MetadataManager
 * ----------------
 * Responsible for maintaining metadata about
 * which nodes store chunks of each file.
 *
 * Metadata is persisted in a file so that
 * it survives program restart.
 */

public class MetadataManager {

    private static final String METADATA_FILE = "metadata.txt";

    private Map<String, List<String>> metadata = new HashMap<>();


    public MetadataManager() {
        loadMetadata();
    }


    /*
     * Adds a node entry for a file chunk
     */
    public void addChunk(String filename, String node) {

        metadata.computeIfAbsent(filename, k -> new ArrayList<>()).add(node);

        saveMetadata();
    }


    /*
     * Returns nodes where chunks of the file exist
     */
    public List<String> getChunks(String filename) {

        return metadata.getOrDefault(filename, new ArrayList<>());
    }


    /*
     * Saves metadata map to file
     */
    private void saveMetadata() {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(METADATA_FILE));

            for (String file : metadata.keySet()) {

                writer.write(file + "=" + String.join(",", metadata.get(file)));
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * Loads metadata from file
     */
    private void loadMetadata() {

        try {

            File file = new File(METADATA_FILE);

            if (!file.exists()) return;

            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("=");

                if (parts.length != 2) continue;

                String filename = parts[0];
                String[] nodes = parts[1].split(",");

                metadata.put(filename, new ArrayList<>(Arrays.asList(nodes)));
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * Debug method
     */
    public void printMetadata() {

        for (String file : metadata.keySet()) {

            System.out.println(file + " -> " + metadata.get(file));
        }
    }
}