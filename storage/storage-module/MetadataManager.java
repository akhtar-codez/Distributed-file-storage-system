import java.util.*;

public class MetadataManager {

    private Map<String, List<String>> metadata = new HashMap<>();

    public void addChunk(String filename, String node) {
        metadata.computeIfAbsent(filename, k -> new ArrayList<>()).add(node);
    }

    public List<String> getChunks(String filename) {
        return metadata.getOrDefault(filename, new ArrayList<>());
    }

    public void printMetadata() {
        for (String file : metadata.keySet()) {
            System.out.println(file + " -> " + metadata.get(file));
        }
    }
}