package com.mickeytheq.hades.core.model.image;

import com.mickeytheq.hades.core.project.ProjectContexts;

import java.awt.image.BufferedImage;

public class ImageProxy {
    private final ImagePersister imagePersister;

    // the identifier provided by the ImagePersister when an image is saved
    // and which is used to load the image
    private String identifier;
    private boolean loaded = false;
    private boolean dirty = false;
    private BufferedImage image;

    private ImageProxy(ImagePersister imagePersister, String identifier) {
        this.imagePersister = imagePersister;
        this.identifier = identifier;

        if (identifier == null)
            loaded = true;
    }

    public String getIdentifier() {
        return identifier;
    }

    public BufferedImage get() {
        if (!loaded)
            load();

        return image;
    }

    public boolean isEmpty() {
        return identifier == null;
    }

    public void set(BufferedImage newImage) {
        if (newImage == null && image == null)
            return;

        image = newImage;
        loaded = true;
        dirty = true;
    }

    private void load() {
        if (identifier == null)
            return;

        image = imagePersister.load(identifier);
        loaded = true;
    }

    public void save() {
        if (!dirty)
            return;

        identifier = imagePersister.save(image, identifier);
    }

    public static ImageProxy createEmpty() {
        return new ImageProxy(getImagePersister(), null);
    }

    public static ImageProxy createFor(String identifier) {
        return new ImageProxy(getImagePersister(), identifier);
    }

    private static ImagePersister getImagePersister() {
        return ProjectContexts.getCurrentContext().getImagePersister();
    }
}
