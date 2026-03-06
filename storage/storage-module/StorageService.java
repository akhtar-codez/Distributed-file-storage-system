public class StorageService {

    private final String BASE_PATH = "storage/node1";

    public StorageService() {
    java.io.File folder = new java.io.File(BASE_PATH);

        if (!folder.exists()) {
            folder.mkdirs();
            System.out.println("Storage folder created at: " + BASE_PATH);
        } else {
            System.out.println("Storage folder already exists.");
        }
    }
    public void saveFile(String sourcePath, String fileName) {

        try {
            java.io.FileInputStream input = new java.io.FileInputStream(sourcePath);
            java.io.FileOutputStream output =
                    new java.io.FileOutputStream(BASE_PATH + "/" + fileName);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            input.close();
            output.close();

            System.out.println("File saved successfully to " + BASE_PATH);

        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}