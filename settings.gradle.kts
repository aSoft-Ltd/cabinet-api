import java.io.File

pluginManagement {
    includeBuild("../build-logic")
}

plugins {
    id("multimodule")
}

fun includeSubs(base: String, path: String = base, vararg subs: String) {
    subs.forEach {
        include(":$base-$it")
        project(":$base-$it").projectDir = File("$path/$it")
    }
}

listOf(
    "cinematic", "keep", "lexi", "neat",
    "symphony", "epsilon", "kollections",
    "koncurrent", "kommander",
).forEach { includeBuild("../$it") }

rootProject.name = "cabinet-api"

includeSubs("cabinet-api", ".", "core", "fake")
