package com.mickeytheq.hades.core.model.image;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

/**
 * {@link ImagePersister} that stores images in a single directory, assigning a random UUID to use as the filename for the image
 */
public class SingleDirectoryUuidEncodedFilenamesImagePersister implements ImagePersister {
    private final Path imageDirectory;

    private static final String HADES_PREFIX = "hades:";

    public SingleDirectoryUuidEncodedFilenamesImagePersister(Path imageDirectory) {
        Objects.requireNonNull(imageDirectory);

        this.imageDirectory = imageDirectory;
    }

    @Override
    public BufferedImage load(String identifier) {
        Objects.requireNonNull(identifier);

        if (!identifier.startsWith(HADES_PREFIX))
            throw new RuntimeException("This image persister only loads identifiers beginning with '" + HADES_PREFIX + "'. Identifier started with '" + StringUtils.substring(identifier, 0, 10) + "'");

        String filename = Strings.CS.removeStart(identifier, HADES_PREFIX);

        Path pathToImage = imageDirectory.resolve(filename);

        try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(pathToImage))) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error reading image from path '" + pathToImage + "'", e);
        }
    }

    @Override
    public String save(BufferedImage image, String existingIdentifier) {
        // no image -> return no identifier
        if (image == null)
            return null;

        String identifier = existingIdentifier;
        String filename;

        // identifier will be null for a new image, assign one
        if (identifier == null) {
            filename = UUID.randomUUID() + ".png";
            identifier = HADES_PREFIX + filename;
        }
        else {
            filename = Strings.CS.removeStart(identifier, HADES_PREFIX);
        }

        Path pathToImage = imageDirectory.resolve(filename);

        try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(pathToImage))) {
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error reading image from path '" + pathToImage + "'", e);
        }

        return identifier;
    }
}
