package com.github.alexeylisyutenko.windowsserviceplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency

/**
 * Windows service plugin.
 */
class WindowsServicePlugin implements Plugin<Project> {

    /**
     * The name of the plugin extension for setting up the plugin.
     */
    static final String EXTENSION_NAME = "windowsService"

    /**
     * The group used for all tasks of this plugin.
     */
    static final String PLUGIN_GROUP = "windows service"

    /**
     * The name of the main plugin's task.
     */
    static final String CREATE_WINDOWS_SERVICE_TASK_NAME = "createWindowsService"

    /**
     * The configuration name used by the plugin for apache commons daemon artifacts.
     */
    static final String COMMONS_DAEMON_BIN_CONFIGURATION_NAME = 'commonsDaemonBin'

    /**
     * The apache commons daemon artifact version.
     */
    static final String COMMONS_DAEMON_BIN_ARTIFACT_VERSION = '1.4.1'

    @Override
    void apply(Project project) {
        // Create an extension.
        project.extensions.create(EXTENSION_NAME, WindowsServicePluginConfiguration)

        // Create configuration for storing binary dependencies.
        Configuration binaryConfig = project.configurations.create(COMMONS_DAEMON_BIN_CONFIGURATION_NAME).setVisible(false)
                .setTransitive(false).setDescription('The apache commons daemon configuration for this project.')

        // Configure dependencies to apache commons daemon windows binaries.
        def commonsDaemonArtifact = "commons-daemon:commons-daemon:${COMMONS_DAEMON_BIN_ARTIFACT_VERSION}:bin-windows@zip"
        Dependency dependency = project.dependencies.create(commonsDaemonArtifact)
        binaryConfig.dependencies.add(dependency)

        // Configure tasks.
        var task = project.tasks.register(CREATE_WINDOWS_SERVICE_TASK_NAME, WindowsServicePluginTask) {
            group = PLUGIN_GROUP
            description = 'Creates a distribution of the program as a windows service application'
        }
    }

}