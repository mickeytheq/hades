package com.mickeytheq.hades.core.project;

import com.mickeytheq.hades.core.model.image.ImagePersister;
import com.mickeytheq.hades.core.model.image.SingleDirectoryUuidEncodedFilenamesImagePersister;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.project.configuration.ProjectConfigurationProviderJson;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class StandardProjectContext implements ProjectContext {
    // TODO: soft reference this to have old ones GCd automatically? not a big deal as probably not many of them
    private static final Map<Path, ProjectContext> existingContexts = new HashMap<>();

    private final ProjectConfiguration projectConfiguration;
    private final ImagePersister imagePersister;

    public StandardProjectContext(ProjectConfiguration projectConfiguration, ImagePersister imagePersister) {
        this.projectConfiguration = projectConfiguration;
        this.imagePersister = imagePersister;
    }

    @Override
    public ProjectConfiguration getProjectConfiguration() {
        return projectConfiguration;
    }

    @Override
    public ImagePersister getImagePersister() {
        return imagePersister;
    }

    public static Optional<ProjectContext> findContextForContentPath(Path contentPath) {
        return HadesProject.findFromPath(contentPath).map(StandardProjectContext::createContext);
    }

    // creates a new ProjectContext, expecting the hades project directory to already exist
    public static ProjectContext getContextForContentPath(Path contentPath) {
        HadesProject hadesProject = HadesProject.getFromPath(contentPath);

        return createContext(hadesProject);
    }

    // creates a new ProjectContext, creating the hades project directory in the given path if required
    public static ProjectContext createContextForStrangeEonsRoot(Path projectRootPath) {
        HadesProject hadesProject = HadesProject.createProjectIfNotExists(projectRootPath);

        return createContext(hadesProject);
    }

    private static ProjectContext createContext(HadesProject hadesProject) {
        // although caching is efficient the primary reason this is cached is to ensure that subsequent calls that resolve to the same
        // Hades project location/root get the same ProjectConfiguration as required by the ProjectContext contract
        return existingContexts.computeIfAbsent(hadesProject.getRootDirectory(), path -> {
            ImagePersister imagePersister = new SingleDirectoryUuidEncodedFilenamesImagePersister(hadesProject.getImagesDirectory());

            // TODO: pretty ugly but can't see anything better right now
            // loading a ProjectConfiguration requires the ImagePersister to be available for deserialising any ImageProxy values
            // but we're in the middle of creating the ProjectContext that provides the ImagePersister
            // work around this by creating a temporary ProjectContext with just the ImagePersister while the Project config
            // is being loaded
            ProjectConfiguration projectConfiguration = ProjectContexts.withContextReturn(
                    new StandardProjectContext(null, imagePersister),
                    () -> new ProjectConfigurationProviderJson(hadesProject.getProjectConfigurationFile()).load()
            );

            return new StandardProjectContext(projectConfiguration, imagePersister);
        });
    }
}
