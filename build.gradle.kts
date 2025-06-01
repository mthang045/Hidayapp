plugins {
    id("com.android.application") version "8.10.1" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
