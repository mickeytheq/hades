package com.mickeytheq.hades.core.model.image;

import java.awt.image.BufferedImage;

// stateless persister of images using a unique identifier
public interface ImagePersister {
    // load a given identifier. identifier must not be null
    BufferedImage load(String identifier);

    // save an image with an optional existing identifier
    // if null is passed in as the existing identifier a new identifier will be generated
    // either way the image's identifier is returned
    String save(BufferedImage image, String existingIdentifier);
}
