package com.github.alexeylisyutenko.windowsserviceplugin

import groovy.transform.Canonical
import groovy.transform.Immutable
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional

import javax.inject.Inject

/**
 * Class which contains all settings needed for creating a windows service.
 */
class WindowsServicePluginConfiguration {

    /**
     * An output directory where results will be placed.
     */
    @Input
    final Property<String> outputDir

    /**
     * Service executable architecture.
     */
    @Input
    final Property<Architecture> architecture

    /**
     * A service description.
     */
    @Input
    @Optional
    final Property<String> description

    /**
     * Service display name.
     */
    @Input
    final Property<String> displayName

    /**
     * A class name that contains the startup method.
     */
    @Input
    final Property<String> startClass

    /**
     * A name of method to be called when a service is started.
     */
    @Input
    final Property<String> startMethod

    /**
     * A list of parameters that will be passed to StartClass.
     *
     * <p>
     * This field could be set in two ways:
     * <ul>
     * <li>As a string where parameters are separated using either # or ; character:
     * <blockquote><pre>windowsService.startParams='startParam1;startParam2'</pre></blockquote>
     * <li>As a list:
     * <blockquote><pre>windowsService.startParams=['startParam1', 'startParam2']</pre></blockquote>
     * </ul>
     * </p>
     */
    @Input
    @Optional
    final Property<Object> startParams

    /**
     * A class name that will be used on Stop service signal.
     */
    @Input
    final Property<String> stopClass

    /**
     * A name of method to be called when service is stopped.
     */
    @Input
    final Property<String> stopMethod

    /**
     * A list of parameters that will be passed to StopClass.
     *
     * <p>
     * This field could be set in two ways:
     * <ul>
     * <li>As a string where parameters are separated using either # or ; character:
     * <blockquote><pre>windowsService.stopParams='stopParam1;stopParam2'</pre></blockquote>
     * <li>As a list:
     * <blockquote><pre>windowsService.stopParams=['stopParam1', 'stopParam2']</pre></blockquote>
     * </ul>
     * </p>
     */
    @Input
    @Optional
    final Property<Object> stopParams

    /**
     * A startup mode for a service.
     */
    @Input
    final Property<Startup> startup

    /**
     * Service type can be interactive to allow the service to interact with the desktop.
     */
    @Input
    @Optional
    final Property<Boolean> interactive

    /**
     * List of services that this service depends on.
     *
     * <p>
     * This field could be set in two ways:
     * <ul>
     * <li>As a string where parameters are separated using either # or ; character:
     * <blockquote><pre>windowsService.dependsOn='WindowsServiceOne;WindowsServiceTwo'</pre></blockquote>
     * <li>As a list:
     * <blockquote><pre>windowsService.dependsOn=['WindowsServiceOne', 'WindowsServiceTwo']</pre></blockquote>
     * </ul>
     * </p>
     */
    @Input
    @Optional
    final Property<Object> dependsOn

    /**
     * List of environment variables that will be provided to the service in the form key=value.
     *
     * <p>
     * This field could be set in two ways:
     * <ol>
     * <li>As a string where key-value pairs are separated using either # or ; characters:
     * <blockquote><pre>windowsService.environment='envKey1=value1;envKey2=value2'</pre></blockquote>
     * <li>As a map:
     * <blockquote><pre>windowsService.environment=['key1': 'value1', 'key2': 'value2']</pre></blockquote>
     * </ol>
     * </p>
     *
     * Note: If you use the first option and you need to embed either # or ; character within a value put them inside single quotes.
     */
    @Input
    @Optional
    final Property<Object> environment

    /**
     * Directory added to the search path used to locate the DLLs for the JVM. This directory is added both in front of
     * the PATH environment variable and as a parameter to the SetDLLDirectory function.
     */
    @Input
    @Optional
    final Property<String> libraryPath

    /**
     * Set a different JAVA_HOME than defined by JAVA_HOME environment variable.
     */
    @Input
    @Optional
    final Property<String> javaHome

    /**
     * Use either auto (i.e. find the JVM from the Windows registry) or specify the full path to the jvm.dll.
     * You can use environment variable expansion here.
     */
    @Input
    @Optional
    final Property<String> jvm

    /**
     * List of options in the form of -D or -X that will be passed to the JVM.
     *
     * <p>
     * This field could be set in two ways:
     * <ol>
     * <li>As a string where parameters are separated using either # or ; character:
     * <blockquote><pre>windowsService.jvmOptions='jvmOption1;jvmOption2'</pre></blockquote>
     * <li>As a list:
     * <blockquote><pre>windowsService.jvmOptions=['jvmOption1', 'jvmOption2']</pre></blockquote>
     * </ol>
     * </p>
     *
     * Note: If you use the first option and you need to embed either # or ; character within a value put them inside single quotes.
     */
    @Input
    @Optional
    final Property<Object> jvmOptions

    /**
     * List of options in the form of -D or -X that will be passed to the JVM when running on Java 9 or later.
     *
     * <p>
     * This field could be set in two ways:
     * <ol>
     * <li>As a string where parameters are separated using either # or ; character:
     * <blockquote><pre>windowsService.jvmOptions9='jvmOption1;jvmOption2'</pre></blockquote>
     * <li>As a list:
     * <blockquote><pre>windowsService.jvmOptions9=['jvmOption1', 'jvmOption2']</pre></blockquote>
     * </ol>
     * </p>
     *
     * Note: If you use the first option and you need to embed either # or ; character within a value put them inside single quotes.
     */
    @Input
    @Optional
    final Property<Object> jvmOptions9

    /**
     * Initial memory pool size in MB.
     */
    @Input
    @Optional
    final Property<Integer> jvmMs

    /**
     * Maximum memory pool size in MB.
     */
    @Input
    @Optional
    final Property<Integer> jvmMx

    /**
     * Thread stack size in KB.
     */
    @Input
    @Optional
    final Property<Integer> jvmSs

    /**
     * Defines the timeout in seconds that procrun waits for service to exit gracefully.
     */
    @Input
    @Optional
    final Property<Integer> stopTimeout

    /**
     * Defines the path for logging. Creates the directory if necessary.
     */
    @Input
    @Optional
    final Property<String> logPath

    /**
     * Defines the service log filename prefix. The log file is created in the LogPath directory with
     * .YEAR-MONTH-DAY.log suffix.
     */
    @Input
    @Optional
    final Property<String> logPrefix

    /**
     * Defines the logging level and can be either Error, Info, Warn or Debug.
     */
    @Input
    @Optional
    final Property<LogLevel> logLevel

    /**
     * Set this non-zero (e.g. 1) to capture JVM jni debug messages in the procrun log file.
     * Is not needed if stdout/stderr redirection is being used.
     */
    @Input
    @Optional
    final Property<Integer> logJniMessages

    /**
     * Redirected stdout filename. If named auto file is created inside LogPath with the name
     * service-stdout.YEAR-MONTH-DAY.log.
     */
    @Input
    @Optional
    final Property<String> stdOutput

    /**
     * Redirected stderr filename. If named auto file is created in the LogPath directory with the name
     * service-stderr.YEAR-MONTH-DAY.log.
     */
    @Input
    @Optional
    final Property<String> stdError

    /**
     * Defines the file name for storing the running process id. Actual file is created in the LogPath directory.
     */
    @Input
    @Optional
    final Property<String> pidFile

    /**
     * Use this property to override the classpath if the default classpath does not provide the results you want.
     */
    @InputFiles
    @Optional
    final ConfigurableFileCollection overridingClasspath

    /**
     * Specifies the name of the account under which the service should run. Use an account name in the form
     * DomainName\UserName. The service process will be logged on as this user. if the account belongs to the built-in
     * domain, you can specify .\UserName.
     */
    @Input
    @Optional
    final Property<String> serviceUser

    /**
     * Password for user account set by serviceUser parameter.
     */
    @Input
    @Optional
    final Property<String> servicePassword

    @Inject
    WindowsServicePluginConfiguration(ObjectFactory factory) {
        outputDir = factory.property(String).convention('windows-service')
        architecture = factory.property(Architecture).convention(Architecture.AMD64)
        description = factory.property(String)
        displayName = factory.property(String)
        startClass = factory.property(String)
        startMethod = factory.property(String)
        startParams = factory.property(Object)
        stopClass = factory.property(String)
        stopMethod = factory.property(String)
        stopParams = factory.property(Object)
        startup = factory.property(Startup).convention(Startup.MANUAL)
        interactive = factory.property(Boolean)
        dependsOn = factory.property(String)
        environment = factory.property(String)
        libraryPath = factory.property(String)
        javaHome = factory.property(String)
        jvm = factory.property(String).convention('auto')
        jvmOptions = factory.property(Object)
        jvmOptions9 = factory.property(Object)
        jvmMs = factory.property(Integer)
        jvmMx = factory.property(Integer)
        jvmSs = factory.property(Integer)
        stopTimeout = factory.property(Integer)
        logPath = factory.property(String)
        logPrefix = factory.property(String)
        logLevel = factory.property(LogLevel)
        logJniMessages = factory.property(Integer)
        stdOutput = factory.property(String)
        stdError = factory.property(String)
        pidFile = factory.property(String)
        overridingClasspath = factory.fileCollection()
        serviceUser = factory.property(String)
        servicePassword = factory.property(String)
    }
}
