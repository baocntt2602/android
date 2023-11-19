plugins {
  id("nexxtidea.android.library")
  id("nexxtidea.android.hilt")
}

android {
    namespace = "com.nexxtidea.sample.domain"

  buildFeatures {
    buildConfig = true
  }

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {
  api(project(":core:utils"))

  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.guava)
  implementation(libs.kotlin.stdlib)
  implementation(libs.core.ktx)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
}
