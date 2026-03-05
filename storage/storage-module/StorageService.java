import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class StorageService {

    private static final String STORAGE_DIR = "storage/";

    public StorageService() {
        File dir = new File(STORAGE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    // Upload file
    public void uploadFile(String sourcePath) throws IOException {

        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            System.out.println("File not found.");
            return;
        }

        Path destination = Path.of(STORAGE_DIR + sourceFile.getName());

        Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File uploaded successfully: " + sourceFile.getName());
    }

    // Download file
    public void downloadFile(String fileName, String destinationPath) throws IOException {

        Path source = Path.of(STORAGE_DIR + fileName);
        Path destination = Path.of(destinationPath + fileName);

        if (!Files.exists(source)) {
            System.out.println("File does not exist in storage.");
            return;
        }

        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File downloaded successfully.");
    }
}