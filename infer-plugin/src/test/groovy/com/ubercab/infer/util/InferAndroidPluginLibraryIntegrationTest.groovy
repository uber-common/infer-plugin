package com.ubercab.infer.util

import org.junit.Before
import org.junit.Test

class InferAndroidPluginLibraryIntegrationTest extends IntegrationTest {

    String androidTestBuildFile

    @Before
    void setup() {
        def pluginClasspath = TestUtils.getPluginClasspath(this)
        androidTestBuildFile = """
                buildscript {
                    repositories {
                        jcenter()
                        mavenLocal()
                    }

                    dependencies {
                        classpath 'com.android.tools.build:gradle:1.5.0'
                        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
                        classpath files($pluginClasspath)
                    }
                }

                apply plugin: 'com.android.library'
                apply plugin: 'com.neenbedankt.android-apt'
                apply plugin: 'com.ubercab.infer.android'

                repositories {
                    jcenter()
                }

                android {
                    buildToolsVersion "23.0.2"
                    compileSdkVersion 23

                    defaultConfig {
                        minSdkVersion 23
                        targetSdkVersion 23
                    }

                    dependencies {
                        compile 'com.intellij:annotations:5.1'

                        // Annotations included to test provided support.
                        provided 'javax.annotation:jsr250-api:1.0'

                        // Leak Canary included to test AAR support.
                        debugCompile 'com.squareup.leakcanary:leakcanary-android:1.3.1'
                        releaseCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.3.1'
                        testCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.3.1'
                    }
                }

                inferPlugin {
                    eradicate {
                        exclude.plus("src")
                        include.plus("src")
                    }

                    infer {
                        exclude.plus("src")
                        include.plus("src")
                    }
                }
            """
    }

    @Test
    void eradicateDebug_withBadSource_shouldFailWhenInferFindsAWarning() {
        runCommand("eradicateDebug", "failing_eradicate_android_project", false)
    }

    @Test
    void eradicateRelease_withBadSource_shouldFailWhenInferFindsAWarning() {
        runCommand("eradicateRelease", "failing_eradicate_android_project", false)
    }

    @Test
    void eradicateDebug_withGoodSource_shouldPassWhenInferFindsNoWarnings() {
        runCommand("eradicateDebug", "passing_eradicate_android_project", true)
    }

    @Test
    void eradicateRelease_withGoodSource_shouldPassWhenInferFindsNoWarnings() {
        runCommand("eradicateRelease", "passing_eradicate_android_project", true)
    }

    @Test
    void inferDebug_withBadSource_shouldFailWhenInferFindsAWarning() {
        runCommand("inferDebug", "failing_infer_android_project", false)
    }

    @Test
    void inferRelease_withBadSource_shouldFailWhenInferFindsAWarning() {
        runCommand("inferRelease", "failing_infer_android_project", false)
    }

    @Test
    void inferDebug_withGoodSource_shouldPassWhenInferFindsNoWarnings() {
        runCommand("inferDebug", "passing_infer_android_project", true)
    }

    @Test
    void inferRelease_withGoodSource_shouldPassWhenInferFindsNoWarnings() {
        runCommand("inferRelease", "passing_infer_android_project", true)
    }

    private def runCommand(String command, String fixtureName, boolean shouldSucceed) {
        runCommand(command, fixtureName, androidTestBuildFile, shouldSucceed)
    }
}
