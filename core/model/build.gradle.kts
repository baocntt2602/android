plugins {
  id("nexxtidea.android.library")
  id("nexxtidea.android.hilt")
}

android {
  namespace = "com.nexxtidea.sample.model"

  buildFeatures {
    buildConfig = true
  }

  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

}

dependencies {
  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.datetime)

  implementation(libs.jackson.core)
  implementation(libs.jackson.kotlin)
}
