package com.passioplatformsdkbridge

import ai.passio.passiosdk.core.config.*
import ai.passio.passiosdk.platform.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class PassioPlatformSDKBridgeModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "PassioPlatformSDKBridge"

    private val mainHandler = Handler(Looper.getMainLooper())

    @ReactMethod
    fun configure(
        key: String,
        projectID: String,
        debugMode: Int,
        autoUpdate: Boolean,
        promise: Promise
    ) {
        mainHandler.post {
            Log.d("PassioPlatformSDK","configure")
            val config = PassioConfiguration(reactApplicationContext, key).apply {
                this.debugMode = debugMode
                this.projectId = projectID
            }

            PassioSDK.instance.setPassioStatusListener(object : PassioStatusListener {
                override fun onCompletedDownloadingAllFiles(fileUris: List<Uri>) {
                }

                override fun onCompletedDownloadingFile(fileUri: Uri, filesLeft: Int) {
                }

                override fun onDownloadError(message: String) {
                }

                override fun onPassioStatusChanged(status: PassioStatus) {
                    when (status.mode) {
                        PassioMode.IS_DOWNLOADING_MODELS -> Log.d("PassioPlatformSDK","Downloading model")
                        PassioMode.IS_BEING_CONFIGURED -> Log.d("PassioPlatformSDK","Configuring")
                        PassioMode.IS_READY_FOR_DETECTION -> promise.resolve("isReadyForDetection")
                        PassioMode.NOT_READY -> promise.resolve("notReady")
                        PassioMode.FAILED_TO_CONFIGURE -> promise.resolve("failedToConfigure")
                    }
                }
            })

            PassioSDK.instance.configure(config) { }
        }
    }

    private val listener = object : ClassificationListener {
        override fun onClassificationResult(candidate: ClassificationCandidate, bitmap: Bitmap) {
            Log.d("PassioPlatformSDK","listener hit")
            val emitter =
                reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            emitter.emit("onDetectionCandidates", candidate.label.displayName)
        }
    }

    @ReactMethod
    fun startDetection() {
        Log.d("PassioPlatformSDK","startDetection")
        PassioSDK.instance.startDetection(listener)
    }

    @ReactMethod
    fun stopDetection() {
        Log.d("PassioPlatformSDK","stopDetection")
        PassioSDK.instance.stopDetection()
    }
}