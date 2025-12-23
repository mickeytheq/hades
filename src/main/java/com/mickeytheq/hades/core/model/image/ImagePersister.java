package com.mickeytheq.hades.core.model.image;

import java.awt.image.BufferedImage;

public interface ImagePersister {
    BufferedImage load(String identifier);

    String save(BufferedImage image, String existingIdentifier);
}
