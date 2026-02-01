package com.mickeytheq.hades.core.project;

import java.util.function.Supplier;

// allows ProjectContext to be placed in a ThreadLocal and accessed via getCurrentContext
// used in a few places where passing the ProjectContext through would be prohibitively messy, for example the ImagePersister
public class ProjectContexts {
    private final static ThreadLocal<ProjectContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static void withContext(ProjectContext projectContext, Runnable runnable) {
        withContextReturn(projectContext, () -> {
            runnable.run();
            return null;
        });
    }

    public static <T> T withContextReturn(ProjectContext projectContext, Supplier<T> runnableResult) {
        ProjectContext currentContext = CONTEXT_THREAD_LOCAL.get();

        // if there's already a current context we should not replace it
        if (currentContext != null) {
            // if we're trying to overwrite a different context
            // then error as this is not allowed
            if (currentContext != projectContext)
                throw new RuntimeException("Attempting to set a different ProjectContext to the one already active. Report to Hades author");

            // the context is already set in the thread local so just execute the runnable
            return runnableResult.get();
        }

        CONTEXT_THREAD_LOCAL.set(projectContext);
        try {
            return runnableResult.get();
        }
        finally {
            CONTEXT_THREAD_LOCAL.remove();
        }
    }

    public static ProjectContext getCurrentContext() {
        ProjectContext projectContext = CONTEXT_THREAD_LOCAL.get();

        if (projectContext == null)
            throw new RuntimeException("No current ProjectContext available");

        return projectContext;
    }
}
