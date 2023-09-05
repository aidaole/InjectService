package com.aidaole.injectservice.plugin

import com.android.build.gradle.AppExtension
import com.aidaole.injectservice.plugin.transform.InjectManagerCreateTransform
import com.aidaole.injectservice.plugin.transform.InjectServiceTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class InjectServicePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("InspectServicePlugin start")
        val appExtension = project.extensions.findByType(AppExtension::class.java)
        appExtension?.registerTransform(InjectServiceTransform())
        appExtension?.registerTransform(InjectManagerCreateTransform())
        println("InspectServicePlugin end")
    }
}