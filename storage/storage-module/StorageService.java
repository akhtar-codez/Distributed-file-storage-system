import java.io.*;

public class StorageService {

    private static final String STORAGE_PATH = "storage/";
    private static final int CHUNK_SIZE = 1024; // 1KB chunk size

    public static void storeFile(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            int chunkNumber = 1;

            while ((bytesRead = fis.read(buffer)) != -1) {

                String node = getNode(chunkNumber);
                String chunkFileName = file.getName() + "_chunk_" + chunkNumber;

                FileOutputStream fos = new FileOutputStream(
                        STORAGE_PATH + node + "/" + chunkFileName);

                fos.write(buffer, 0, bytesRead);
                fos.close();

                System.out.println("Stored " + chunkFileName + " in " + node);

                chunkNumber++;
            }

            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String fileName) throws IOException {

        String outputFile = "downloaded_" + fileName;

        FileOutputStream fos = new FileOutputStream(outputFile);

        for (int i = 1; i <= 3; i++) {

            String nodePath = STORAGE_PATH + "node" + i + "/" + fileName + "_chunk_" + i;

            File chunkFile = new File(nodePath);

            if (!chunkFile.exists()) {
                System.out.println("Chunk missing from node" + i);
                continue;
            }

            FileInputStream fis = new FileInputStream(chunkFile);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            fis.close();

            System.out.println("Retrieved chunk from node" + i);
        }

        fos.close();

        System.out.println("File reconstructed successfully as: " + outputFile);
    }

    private static String getNode(int chunkNumber) {

        if (chunkNumber % 3 == 1) {
            return "node1";
        } else if (chunkNumber % 3 == 2) {
            return "node2";
        } else {
            return "node3";
        }
    }
}