package com.github.alexeylisyutenko.windowsserviceplugin.script

import com.github.alexeylisyutenko.windowsserviceplugin.Utils
import com.github.alexeylisyutenko.windowsserviceplugin.WindowsServicePluginConfiguration
import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import org.gradle.api.file.FileCollection

import java.util.stream.Collectors

/**
 * A class which generates batch script for installing a service.
 */
class InstallScriptGenerator {

    private String applicationName
    private FileCollection classpath
    private WindowsServicePluginConfiguration configuration
    private File outputDirectory

    InstallScriptGenerator(String applicationName, FileCollection classpath, WindowsServicePluginConfiguration configuration, File outputDirectory) {
        this.applicationName = applicationName
        this.classpath = classpath
        this.configuration = configuration
        this.outputDirectory = outputDirectory
    }

    void generate() {
        def binding = [
                applicationName: applicationName,
                serviceExeName : applicationName + ".exe",
                classpath      : JoinedClasspathBuilder.build(classpath),
                installOptions : InstallOptionsBuilder.build(configuration)
        ]
        generateOutputFor(binding)
    }

    private void generateOutputFor(Map<String, String> binding) {
        def reader = Utils.getResourceReader(this.getClass(), "installScript.txt")
        def engine = new SimpleTemplateEngine()
        Template template = engine.createTemplate(reader)

        new File(outputDirectory, "${applicationName}-install.bat").withWriter { writer ->
            String output = template.make(binding).toString()
            writer.write(Utils.convertLineSeparatorsToWindows(output))
        }
    }

    private static class JoinedClasspathBuilder {
        static String build(FileCollection classpath) {
            classpath.files
                    .collect { file -> '%APP_HOME%lib\\' + file.getName() }
                    .join(';')
        }
    }

    private static class InstallOptionsBuilder {

        static String build(WindowsServicePluginConfiguration configuration) {
            Map<String, String> options = createInstallOptionsMapFor(configuration)
            options.entrySet().stream()
                    .filter { it.value != null }
                    .map { it.key + '=' + addQuotesIfNeeded(it.value) }
                    .collect(Collectors.joining(' ^\r\n    ', '^\r\n    ', ''))
        }

        private static Map<String, String> createInstallOptionsMapFor(WindowsServicePluginConfiguration configuration) {
            def options = [
                    "--Classpath"      : "%CLASSPATH%",
                    "--Description"    : configuration.description.getOrNull(),
                    "--DisplayName"    : configuration.displayName.getOrNull(),
                    "--StartClass"     : configuration.startClass.getOrNull(),
                    "--StartMethod"    : configuration.startMethod.getOrNull(),
                    "++StartParams"    : MultiValueParameterConverter.convertToString(configuration.startParams.getOrNull()),
                    "--StartMode"      : "jvm",
                    "--StopClass"      : configuration.stopClass.getOrNull(),
                    "--StopMethod"     : configuration.stopMethod.getOrNull(),
                    "++StopParams"     : MultiValueParameterConverter.convertToString(configuration.stopParams.getOrNull()),
                    "--StopMode"       : "jvm",
                    "--Jvm"            : toWindowsPath(configuration.jvm.getOrNull()),
                    "--Startup"        : configuration.startup.getOrNull().name().toLowerCase(),
                    "--Type"           : configuration.interactive.getOrNull() ? "interactive" : null,
                    "++DependsOn"      : MultiValueParameterConverter.convertToString(configuration.dependsOn.getOrNull()),
                    "++Environment"    : MultiValueParameterConverter.convertToString(configuration.environment.getOrNull()),
                    "--LibraryPath"    : toWindowsPath(configuration.libraryPath.getOrNull()),
                    "--JavaHome"       : toWindowsPath(configuration.javaHome.getOrNull()),
                    "++JvmOptions"     : MultiValueParameterConverter.convertToString(configuration.jvmOptions.getOrNull()),
                    "++JvmOptions9"    : MultiValueParameterConverter.convertToString(configuration.jvmOptions9.getOrNull()),
                    "--JvmMs"          : configuration.jvmMs.getOrNull()?.toString(),
                    "--JvmMx"          : configuration.jvmMx.getOrNull()?.toString(),
                    "--JvmSs"          : configuration.jvmSs.getOrNull()?.toString(),
                    "--StopTimeout"    : configuration.stopTimeout.getOrNull()?.toString(),
                    "--LogPath"        : toWindowsPath(configuration.logPath.getOrNull()),
                    "--LogPrefix"      : configuration.logPrefix.getOrNull(),
                    "--LogLevel"       : configuration.logLevel.getOrNull()?.name()?.toLowerCase()?.capitalize(),
                    "--LogJniMessages" : configuration.logJniMessages.getOrNull()?.toString(),
                    "--StdOutput"      : configuration.stdOutput.getOrNull(),
                    "--StdError"       : configuration.stdError.getOrNull(),
                    "--PidFile"        : configuration.pidFile.getOrNull(),
                    "--ServiceUser"    : configuration.serviceUser.getOrNull(),
                    "--ServicePassword": configuration.servicePassword.getOrNull()
            ]
            options
        }

        private static String toWindowsPath(String path) {
            path?.replace('/', '\\')
        }

        private static Object addQuotesIfNeeded(String value) {
            value.contains(" ") ? "\"" + value + "\"" : value
        }

    }

}