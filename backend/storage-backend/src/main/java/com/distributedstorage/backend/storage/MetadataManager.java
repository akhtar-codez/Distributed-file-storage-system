package com.distributedstorage.backend.storage;

import java.util.*;

/*
 * MetadataManager
 * ----------------
 * Stores metadata about where file chunks are located.
 *
 * Structure:
 * filename -> chunk number -> list of nodes storing that chunk
 */

public class MetadataManager {

    // filename -> (chunkNumber -> nodes)
    private Map<String, Map<Integer, List<String>>> metadata = new HashMap<>();


    /*
     * Adds metadata entry for a chunk
     */
    public void addChunk(String filename, int chunkNumber, String node) {

        metadata
            .computeIfAbsent(filename, k -> new HashMap<>())
            .computeIfAbsent(chunkNumber, k -> new ArrayList<>())
            .add(node);
    }


    /*
     * Returns nodes storing a specific chunk
     */
    public List<String> getChunkNodes(String filename, int chunkNumber) {

        if (!metadata.containsKey(filename)) return new ArrayList<>();

        return metadata.get(filename).getOrDefault(chunkNumber, new ArrayList<>());
    }


    /*
     * Returns all chunk numbers for a file
     */
    public Set<Integer> getChunks(String filename) {

        if (!metadata.containsKey(filename)) return new HashSet<>();

        return metadata.get(filename).keySet();
    }


    /*
     * Debug method to print metadata
     */
    public void printMetadata() {

        System.out.println("----- METADATA -----");

        for (String file : metadata.keySet()) {

            System.out.println("File: " + file);

            Map<Integer, List<String>> chunks = metadata.get(file);

            for (int chunk : chunks.keySet()) {

                System.out.println(
                        "Chunk_" + chunk + " -> " + chunks.get(chunk)
                );
            }
        }
    }
}