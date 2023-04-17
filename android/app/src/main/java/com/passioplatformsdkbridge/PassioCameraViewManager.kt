package com.passioplatformsdkbridge

import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext

class PassioCameraViewManager: SimpleViewManager<PassioCameraView>() {

  override fun getName() = "PassioCameraView"

  override fun createViewInstance(reactContext: ThemedReactContext): PassioCameraView {
    val activity = reactContext.currentActivity as AppCompatActivity
    return PassioCameraView(reactContext, activity)
  }
}
