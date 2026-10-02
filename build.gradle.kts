plugins {
    java
    id("net.fabricmc.fabric-loom-remap") version("1.17.+")
    id("ploceus") version("1.17.+")
}

group = "dev.rdh"
version = providers.environmentVariable("GITHUB_SHA")
    .flatMap { sha -> providers.gradleProperty("mod_version").map { "$it+${sha.take(7)}" } }
    .orElse(providers.gradleProperty("mod_version"))
    .get()

java.toolchain {
    languageVersion = JavaLanguageVersion.of(25)
}

@Suppress("MayBeConstant")
object Versions {
    val minecraft = "1.8.9"
    val feather = "2"
    val osl = "0.21.1"
    val fabric = "0.19.5"
    val celeritas = "2.5.0-pre.1"
    val netty = "4.2.18.Final"
}

loom {
    accessWidenerPath = file("src/main/resources/sarcio.classtweaker")

    runs.named("client") {
        jvmArguments.add("-Dmixin.debug.export=true")
    }
}

ploceus {
    setIntermediaryGeneration(2)
}

val celery = sourceSets.create("celery") {
    compileClasspath += sourceSets.main.map { it.output + it.compileClasspath }.get()
}

repositories {
    mavenCentral()
    maven("https://maven.taumc.org/releases")
}

val nettyModules = listOf(
    "common", "buffer", "transport", "resolver", "codec-base", "handler",
    "codec-dns", "resolver-dns", "transport-native-unix-common", "transport-classes-epoll",
)

configurations.configureEach {
    exclude(group = "io.netty", module = "netty-all")
    exclude(group = "com.mojang", module = "netty")
}

dependencies {
    minecraft("com.mojang:minecraft:${Versions.minecraft}")
    mappings(ploceus.featherMappings(Versions.feather))

    modImplementation("net.fabricmc:fabric-loader:${Versions.fabric}")
    ploceus.dependOsl(Versions.osl)
    nettyModules.forEach { include(implementation("io.netty:netty-$it:${Versions.netty}")!!) }
    include(implementation("io.netty:netty-resolver-dns-classes-macos:${Versions.netty}")!!)
    listOf("osx-x86_64", "osx-aarch_64").forEach { include(runtimeOnly("io.netty:netty-resolver-dns-native-macos:${Versions.netty}:$it")!!) }
    listOf("linux-x86_64", "linux-aarch_64", "linux-riscv64").forEach { include(runtimeOnly("io.netty:netty-transport-native-epoll:${Versions.netty}:$it")!!) }
    add(celery.compileOnlyConfigurationName, "org.embeddedt.celeritas:celeritas-common:${Versions.celeritas}")
}

tasks.assemble {
    dependsOn("remapJar")
}

tasks.jar {
    from(celery.output)
}

tasks.processResources {
    val v = project.version
    inputs.property("version", v)

    filesMatching("fabric.mod.json") {
        expand("version" to v)
    }
}
