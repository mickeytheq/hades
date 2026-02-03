package com.mickeytheq.hades.core.model.image;

import com.mickeytheq.hades.core.model.entity.NeedsDeepCopy;
import com.mickeytheq.hades.core.project.ProjectContexts;

import java.awt.image.BufferedImage;

// a wrapper around a loadable image
// delegates loading/saving to the provided ImagePersister
// provides lazy loading of images so that loading a container such as a Card/CardFaceModel does not
// load all the heavy image data immediately
public class ImageProxy implements NeedsDeepCopy {
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

    private ImageProxy(BufferedImage image) {
        this.imagePersister = null;
        this.image = image;

        loaded = true;
    }

    public BufferedImage get() {
        if (!loaded)
            load();

        return image;
    }

    public boolean isEmpty() {
        return identifier == null && image == null;
    }

    public void set(BufferedImage newImage) {
        if (newImage == null && image == null)
            return;

        // if the image is changed we wiped the cached identifier
        // identifiers correlate with the persisted image so when the image is changed a new identifier must be generated
        // this also allows copying of ImageProxies without changing the identifier
        identifier = null;

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

    public String save() {
        if (!dirty)
            return identifier;

        identifier = imagePersister.save(image, identifier);

        return identifier;
    }

    // creates an empty proxy that will load nothing
    public static ImageProxy createEmpty() {
        return new ImageProxy(getImagePersister(), null);
    }

    // creates a proxy for an existing identifier
    public static ImageProxy createFor(String identifier) {
        return new ImageProxy(getImagePersister(), identifier);
    }

    // creates a proxy with a specific image - primarily useful for testing
    public static ImageProxy createStatic(BufferedImage image) {
        return new ImageProxy(image);
    }

    private static ImagePersister getImagePersister() {
        return ProjectContexts.getCurrentContext().getImagePersister();
    }

    // ImageProxies aren't shared between different owners
    // identifiers can be shared as once an identifier is assigned it never changes
    @Override
    public Object deepCopy() {
        return new ImageProxy(imagePersister, identifier);
    }
}
