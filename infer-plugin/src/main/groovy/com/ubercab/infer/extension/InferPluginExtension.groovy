package com.ubercab.infer.extension

import com.ubercab.infer.Constants
import org.gradle.api.Project

class InferPluginExtension {
    EradicateExtension eradicate
    InferExtension infer

    InferPluginExtension(Project project) {
        eradicate = project.extensions.create(Constants.EXTENSION_ERADICATE_NAME, EradicateExtension, project)
        infer = project.extensions.create(Constants.EXTENSION_INFER_NAME, InferExtension, project)
    }
}
