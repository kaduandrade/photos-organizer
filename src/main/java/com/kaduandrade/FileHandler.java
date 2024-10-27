package com.kaduandrade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHandler {

    private final String sourceFolder;
    private static final List<FileTypes> allowedFileTypes;

    static {
        allowedFileTypes = Arrays.asList(FileTypes.JPG, FileTypes.NEF, FileTypes.MOV, FileTypes.AVI);
    }

    public FileHandler(String sourceFolder) {
       this.sourceFolder = sourceFolder;
    }

    public List<File> getSourceFiles() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(sourceFolder))) {
            return paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }
    }

    public static List<FileTypes> getAllowedFileTypes() {
        return allowedFileTypes;
    }
}
