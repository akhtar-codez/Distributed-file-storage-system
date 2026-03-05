import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StorageService {

    private static final String STORAGE_PATH = "storage";

    public void initializeStorage() {

        File baseDir = new File(STORAGE_PATH);

        if (!baseDir.exists()) {
            baseDir.mkdir();
        }

        createNode("node1");
        createNode("node2");
        createNode("node3");

        System.out.println("Storage nodes initialized successfully");
    }

    private void createNode(String nodeName) {

        File nodeDir = new File(STORAGE_PATH + "/" + nodeName);

        if (!nodeDir.exists()) {
            nodeDir.mkdir();
            System.out.println(nodeName + " created");
        } else {
            System.out.println(nodeName + " already exists");
        }

    }

    public void saveFile(String fileName, String content) {

        try {

            File file = new File(STORAGE_PATH + "/node1/" + fileName);

            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();

            System.out.println("File saved in node1: " + fileName);

        } catch (IOException e) {

            System.out.println("Error saving file");
            e.printStackTrace();

        }

    }

    public void saveFileInChunks(String fileName, String content) {

        int chunkSize = 10;  // characters per chunk
        int chunkNumber = 1;

        for (int i = 0; i < content.length(); i += chunkSize) {

            String chunk = content.substring(i, Math.min(content.length(), i + chunkSize));

            String node = getNode(chunkNumber);

            String chunkFileName = fileName + "_chunk_" + chunkNumber;

            writeChunk(node, chunkFileName, chunk);

            chunkNumber++;

        }

    }

    private String getNode(int chunkNumber) {

        if (chunkNumber % 3 == 1) {
            return "node1";
        } else if (chunkNumber % 3 == 2) {
            return "node2";
        } else {
            return "node3";
        }

    }

    private void writeChunk(String node, String fileName, String content) {

        try {

            File file = new File(STORAGE_PATH + "/" + node + "/" + fileName);

            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();

            System.out.println("Chunk stored in " + node + ": " + fileName);

        } catch (IOException e) {

            System.out.println("Error writing chunk");
            e.printStackTrace();

        }

    }

    public String readFile(String fileName) {

        StringBuilder fileContent = new StringBuilder();

        try {

            for (int i = 1; i <= 100; i++) {

                String chunkName = fileName + "_chunk_" + i;

                String node = getNode(i);

                File file = new File(STORAGE_PATH + "/" + node + "/" + chunkName);

                if (!file.exists()) {
                    break;
                }

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    fileContent.append(line);
                }

                reader.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileContent.toString();

    }
}