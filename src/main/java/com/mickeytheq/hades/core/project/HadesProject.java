package com.mickeytheq.hades.core.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Simple container and utilities for locating and providing information on the Hades project directory and contents
 */
public class HadesProject {
    public static final String HADES_ROOT_DIRECTORY_NAME = ".hades";
    public static final String HADES_IMAGES_DIRECTORY_NAME = "images";
    public static final String PROJECT_CONFIGURATION_FILENAME = "project.json";

    private final Path rootDirectory;
    private final Path imagesDirectory;
    private final Path projectConfigurationFile;

    public HadesProject(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.imagesDirectory = rootDirectory.resolve(HADES_IMAGES_DIRECTORY_NAME);
        this.projectConfigurationFile = rootDirectory.resolve(PROJECT_CONFIGURATION_FILENAME);

        // create the hades root directory if needed
        try {
            if (!Files.exists(rootDirectory))
                Files.createDirectories(rootDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Error creating Hades project directory at path '" + rootDirectory + "'", e);
        }

        // create the images directory if needed
        try {
            if (!Files.exists(imagesDirectory))
                Files.createDirectories(imagesDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Error creating images directory in Hades project directory at path '" + imagesDirectory + "'", e);
        }
    }

    public Path getRootDirectory() {
        return rootDirectory;
    }

    public Path getImagesDirectory() {
        return imagesDirectory;
    }

    public Path getProjectConfigurationFile() {
        return projectConfigurationFile;
    }

    public static HadesProject getFromPath(Path path) {
        Optional<Path> hadesInternalContentDirectoryPath = findHadesInternalContentDirectory(path);

        if (!hadesInternalContentDirectoryPath.isPresent())
            throw new RuntimeException("No " + HADES_ROOT_DIRECTORY_NAME + " project directory found for path '" + path + "'");

        return new HadesProject(hadesInternalContentDirectoryPath.get());
    }

    public static HadesProject createProjectIfNotExists(Path rootPath) {
        Path hadesProjectDirectory = rootPath.resolve(HADES_ROOT_DIRECTORY_NAME);

        return new HadesProject(hadesProjectDirectory);
    }

    // return the location of the hades internal content for the given path within the regular project (e.g. a card object)
    // TODO: optimise this with some light caching so full path traversal from child to root, searching each level, isn't necessary every time
    // TODO: instead of list use resolve() + exists() as we are looking for a specific directory name
    private static Optional<Path> findHadesInternalContentDirectory(Path contentPath) {
        if (!Files.isDirectory(contentPath))
            return findHadesInternalContentDirectory(contentPath.getParent());

        Optional<Path> hadesProjectDirectory;
        try (Stream<Path> stream = Files.list(contentPath)) {
            hadesProjectDirectory = stream
                    .filter(HadesProject::isHadesProjectDirectory)
                    .findAny();
        } catch (IOException e) {
            throw new RuntimeException("Error listing files in path '" + contentPath + "'", e);
        }

        if (hadesProjectDirectory.isPresent())
            return hadesProjectDirectory;

        Path parent = contentPath.getParent();

        if (parent != null)
            return findHadesInternalContentDirectory(parent);

        return Optional.empty();
    }

    private static boolean isHadesProjectDirectory(Path path) {
        return path.getFileName().toString().equals(HADES_ROOT_DIRECTORY_NAME);
    }
}
