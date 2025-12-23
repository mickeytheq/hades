package com.mickeytheq.hades.core.project;

import java.util.function.Supplier;

public class ProjectContexts {
    private final static ThreadLocal<ProjectContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static void withContext(ProjectContext projectContext, Runnable runnable) {
        CONTEXT_THREAD_LOCAL.set(projectContext);
        try {
            runnable.run();
        }
        finally {
            CONTEXT_THREAD_LOCAL.remove();
        }
    }

    public static <T> T withContextReturn(ProjectContext projectContext, Supplier<T> runnableResult) {
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
