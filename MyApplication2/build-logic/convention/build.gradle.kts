plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationPlugin") {
            id = "myapplication.plugin.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationComposePlugin") {
            id = "myapplication.plugin.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
    }
}