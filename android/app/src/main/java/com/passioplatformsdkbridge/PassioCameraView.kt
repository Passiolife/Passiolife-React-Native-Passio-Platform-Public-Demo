package com.passioplatformsdkbridge

import ai.passio.passiosdk.core.camera.PassioCameraViewProvider
import ai.passio.passiosdk.platform.*
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import androidx.camera.view.PreviewView
import androidx.lifecycle.*
import com.facebook.react.common.LifecycleState


@SuppressLint("ViewConstructor")
class PassioCameraView(context: Context, private val lifecycleOwner: LifecycleOwner): FrameLayout(context), LifecycleOwner, LifecycleObserver, PassioCameraViewProvider {

  private val previewView: PreviewView = PreviewView(context)

  private val registry = LifecycleRegistry(this)

  init {
    Log.d("PassioPlatformSDK","init PassioCameraView")
    previewView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    addView(previewView)
    PassioSDK.instance.startCamera(this)
    lifecycleOwner.lifecycle.addObserver(this)
  }

  private fun resumeCamera() {
    registry.currentState = Lifecycle.State.RESUMED
  }

  private fun stopCamera() {
    registry.currentState = Lifecycle.State.CREATED
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    resumeCamera()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    stopCamera()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onParentLifecycleCreate() {
    resumeCamera()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun onParentLifecycleResume() {
    resumeCamera()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onParentLifecycleStopped() {
    stopCamera()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onParentLifecycleDestroyed() {
    stopCamera()
  }

  override fun requestLayout() {
    super.requestLayout()
    post(measureAndLayout)
  }

  private val measureAndLayout: Runnable = Runnable {
    measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    layout(left, top, right, bottom)
  }

  override fun getLifecycle(): Lifecycle {
    return registry
  }

  override fun requestPreviewView(): PreviewView {
    return previewView
  }

  override fun requestCameraLifecycleOwner(): LifecycleOwner {
    return this
  }

  protected fun finalize() {
    stopCamera()
  }
}
