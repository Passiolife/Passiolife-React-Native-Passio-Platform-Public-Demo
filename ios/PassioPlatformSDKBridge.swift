//
//  PassioPlatformSDKBridge.swift
//  MobileAI
//
//  Created by Winoto, Basuki on 4/12/23.
//

import Foundation
import PassioPlatformSDK

@available(iOS 13.0, *)
@objc(PassioPlatformSDKBridge)
class PassioPlatformSDKBridge: RCTEventEmitter, DetectionDelegate {
  public static var emitter: RCTEventEmitter!

  // init
  override init() {
    super.init()
    PassioPlatformSDKBridge.emitter = self
  }

  override func supportedEvents() -> [String]! {
    return ["onDetectionCandidates"]
  }
  
  @objc
  override class func requiresMainQueueSetup() -> Bool {
    return true
  }

  // SDK bridges
  let passioSDK = PassioPlatformAISDK.shared
  var isDetecting: Bool = false
  
  @objc(configure:projectID:debugMode:autoUpdate:withResolver:withRejecter:)
  func configure(
    key: String, projectID: String, debugMode: Int, autoUpdate: Bool,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock) -> Void {
    var passioConfig = PassioConfiguration(key: key)

    passioConfig.debugMode = debugMode
    passioConfig.projectID = projectID
    passioConfig.overrideInstalledVersion = autoUpdate
      passioSDK.configure(passioConfiguration: passioConfig)
      { status in
        print( "PassioPlatformSDKBridge.configure:\(status)")
        resolve("\(status.mode)")
      }
  }
  
  @objc(startDetection)
  func startDetection() {
    print( "PassioPlatformSDKBridge.startDetection")
    passioSDK.startDetection(detectionDelegate: self) { isReady in
      print( "PassioPlatformSDKBridge.startDetection:\(isReady)")
      self.isDetecting = true
    }
  }

  @objc(stopDetection)
  func stopDetection() {
    print( "PassioPlatformSDKBridge.stopDetection")
    passioSDK.stopDetection()
    self.isDetecting = false
  }
  
  // detection delegate
  func detectionResult(candidates: [PassioPlatformSDK.DetectionCandidate]?, image: UIImage?) {
    print("PassioPlatformSDKBridge.detectionResult:\(String(describing: candidates?[0].label))")
    PassioPlatformSDKBridge.emitter.sendEvent(withName: "onDetectionCandidates", body: candidates?[0].label)
  }
}
