package com.mickeytheq.hades.core.model.image;

import java.awt.image.BufferedImage;

// for testing
public class NothingImagePersister implements ImagePersister {
    @Override
    public BufferedImage load(String identifier) {
        return null;
    }

    @Override
    public String save(BufferedImage image, String existingIdentifier) {
        return "<random UUID>";
    }
}
