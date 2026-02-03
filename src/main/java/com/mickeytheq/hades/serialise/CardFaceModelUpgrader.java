package com.mickeytheq.hades.serialise;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.hades.core.project.ProjectContext;

public interface CardFaceModelUpgrader {
    // perform an in-place upgrade of the JSON node for the card face model
    void upgrade(ProjectContext projectContext, ObjectNode faceNode);
}
