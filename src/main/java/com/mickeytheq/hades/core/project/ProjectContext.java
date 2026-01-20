package com.mickeytheq.hades.core.project;

import com.mickeytheq.hades.core.model.image.ImagePersister;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;

/**
 * Context for a 'project'
 *
 * A standard implementation will be based around a Strange Eons project and the Hades specific directory for that project
 * that holds the project configuration and image files.
 *
 * Alternative implementations may be created to support, for example, testing where a project directory/file structure is onerous to
 * create for transient tests.
 *
 * Multiple ProjectContexts may exist at any one time as Strange Eons supports having cards from multiple projects open simultaneously.
 * Therefore the standard StrangeEons implementation of this cannot be a simple singleton tied to the currently open project but
 * instead must be managed on a 'per-card' basis
 */
public interface ProjectContext {
    /**
     * Returns the {@link ProjectConfiguration} for this context. This must return the same entity (by identity ==) for all
     * calls for the same project. Put more simply, two cards in the same Strange Eons project must see the same instance
     * of {@link ProjectConfiguration}
     */
    ProjectConfiguration getProjectConfiguration();

    /**
     * Returns the {@link ImagePersister} for this project that provides load/save capability for images
     */
    ImagePersister getImagePersister();
}
