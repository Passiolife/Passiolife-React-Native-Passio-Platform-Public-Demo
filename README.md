# Quick Start with Sample App
## Steps to use this sample app on iOS
1. Open file `App.tsx` and edit with the correct license key and projectID.
2. Open `MobileAI.xcworkspace` with xcode.
3. Drag and drop model folder into MobileAI project. Choose `Copy if needed` and `Create groups` in the dialog.
4. Build and run the project to an iOS device.

## Steps to use this sample app on Android
1. Open file `App.tsx` and edit with the correct license key and projectID.
2. Create a new folder `android/app/src/main/assets`. Copy the model files into this folder.
3. Build and run the project to an Android device.


# Custom implementation with blank React Native project
## Steps to integrate PassioPlatformSDK for iOS on a blank React Native project
1. Open the workspace with xcode.
2. Drag and drop the `PlatformSDK.xcframework` into your project. Make sure to select `Copy items if needed`.
3. In project `General` -> `Frameworks, Libraries and Embedded Content` Change to `Embed & Sign`
4. Edit your Info.plist
 ```XML
`<key>NSCameraUsageDescription</key><string>For real-time recognition</string>`.
```

5. Create a new Swift file (Ctrl + n), put the name `PassioPlatformSDKBridge`
6. Click `Create Bridging Header` on the next dialog
7. Edit the newly create Bridging Header file `<ProjectName>-Bridging-Header.h`:
```C
#import <React/RCTBridgeModule.h>
#import <React/RCTViewManager.h>
#import <React/RCTEventEmitter.h>
```
8. Edit `PassioPlatformSDKBridge.swift`:
```Swift
import Foundation
import PassioPlatformSDK

@objc(PassioPlatformSDKBridge)
class PassioPlatformSDKBridge: RCTEventEmitter {

  override func supportedEvents() -> [String]! {
    return []
  }

  @objc
  override class func requiresMainQueueSetup() -> Bool {
    return true
  }

}
```
9. Create a new Objective-C file (Ctrl-n), put the name `PassioPlatformSDKBridge`
```Objective-C
#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(PassioPlatformSDKBridge, RCTEventEmitter)

@end
```
10. Run the app. At this point, the PassioPlatformSDK is exposed to React Native as `NativeModules.PassioPlatformSDK`. But none of the functions are exposed yet.
11. To expose a function, you will need to add a wrapper function in `PassioPlatformSDKBridge.swift` and expose it with RCT_EXTERN_METHOD in `PassioPlatformSDKBridge.m`
12. For reference, this sample app has implemented `PassioPlatformSDKBridge.swift`, `PassioPlatformSDKBridge.m`, `PassioCameraView`, `PassioCameraViewManager`, and `App.tsx`


## Steps to integrate PassioPlatformSDK for Android on a blank React Native project
1. Open the android folder with Android Studio app.
2. Create a new Android Resource Directory `assets` in the app.
3. Copy model files to `assets` directory.
4. Edit project's `build.gradle` file:
```Java
buildscript {
    ext {
        ...
        minSdkVersion = 26
        kotlin_version = "1.6.20"
        ...
    }
    ...
    dependencies {
        ...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "org.jetbrains.kotlin:kotlin-android-extensions:$kotlin_version"
        ...
    }
}
```
5. Edit app's `build.gradle` file:
```Java
apply plugin: 'kotlin-android'
...
dependencies {
    ...
    // Passio SDK
    implementation 'ai.passio.passiosdk:platformsdk:+'

    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
}
```
6. Create a new folder `app/java/com/passioplatformsdkbridge`.
7. Create a new module file `PassioPlatformSDKBridgeModule.kt` under the new folder:
```Kotlin
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

}
```
8. Create a new package file `PassioPlatformSDKBridge.kt` in the same folder:
```Kotlin
package com.passioplatformsdkbridge

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class PassioPlatformSDKBridge : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(PassioPlatformSDKBridgeModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf(PassioCameraViewManager())
    }
}
```
9. Edit `MainApplication.java` to register the package:
```Java
        ...
        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          packages.add(new PassioPlatformSDKBridge());
          return packages;
        }
        ...
```
10. Run the app. At this point, the PassioPlatformSDK is exposed to React Native as `NativeModules.PassioPlatformSDK`. But none of the functions are exposed yet.
11. To expose a function, you will need to expose the function in `PassioPlatformSDKBridgeModule.kt`.
12. For reference, this sample app has implemented `PassioPlatformSDKBridgeModule.kt`, `PassioPlatformSDKBridge.kt`, `PassioCameraView`, `PassioCameraViewManager`, and `App.tsx`
