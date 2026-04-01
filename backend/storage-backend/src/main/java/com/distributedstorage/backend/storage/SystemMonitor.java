package com.distributedstorage.backend.storage;

import java.io.File;
import java.util.List;

/*
 * SystemMonitor
 * -------------
 * Provides system status information for the
 * distributed storage system.
 *
 * Displays:
 * - Available nodes
 * - Number of chunks per node
 * - Total files stored
 */

public class SystemMonitor {

    private static final String STORAGE_PATH = "storage/";

    public static void showSystemStatus() {

        System.out.println("\n------ SYSTEM STATUS ------");

        List<String> nodes = NodeManager.getAvailableNodes();

        System.out.println("\nNodes Available: " + nodes.size());

        int totalChunks = 0;

        for (String node : nodes) {

            File nodeDir = new File(STORAGE_PATH + node);

            File[] files = nodeDir.listFiles();

            int chunkCount = (files == null) ? 0 : files.length;

            totalChunks += chunkCount;

            System.out.println(node + " -> " + chunkCount + " chunks stored");
        }

        System.out.println("\nTotal Chunks Stored: " + totalChunks);
    }
}