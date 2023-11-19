plugins {
  id("nexxtidea.android.library")
  id("nexxtidea.android.library.compose")
}

android {
  namespace = "com.nexxtidea.sample.utils"

  buildFeatures {
    buildConfig = true
  }

  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}

dependencies {
  implementation(libs.activity.compose)
  implementation(libs.compose.foundation)
  implementation(platform(libs.compose.bom))
  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.material3)
  implementation(libs.ui)
  implementation(libs.ui.graphics)
  implementation(libs.ui.tooling.preview)
  implementation(libs.ui.tooling)
}
