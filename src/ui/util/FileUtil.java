package ui.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static final String DATA_FOLDER = "data";

    public static void ensureDataFolderExists() {
        try {
            Path path = Paths.get(DATA_FOLDER);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.out.println("Error creating data folder: " + e.getMessage());
        }
    }

    public static List<String> readLines(String fileName) {
        ensureDataFolderExists();
        Path path = Paths.get(DATA_FOLDER, fileName);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return new ArrayList<String>();
            }
            return Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("Error reading file " + fileName + ": " + e.getMessage());
            return new ArrayList<String>();
        }
    }

    public static void writeLines(String fileName, List<String> lines) {
        ensureDataFolderExists();
        Path path = Paths.get(DATA_FOLDER, fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file " + fileName + ": " + e.getMessage());
        }
    }
}
