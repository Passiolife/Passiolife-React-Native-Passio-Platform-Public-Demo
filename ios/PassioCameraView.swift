//
//  PassioCameraView.swift
//  MobileAI
//
//  Created by Winoto, Basuki on 4/13/23.
//

import Foundation
import UIKit
import AVFoundation
import PassioPlatformSDK

@available(iOS 13.0, *)
@objc
public class PassioCameraView: UIView {
  private let previewLayer: AVCaptureVideoPreviewLayer?

  public override init(frame: CGRect) {
    NSLog("init get preview layer")
    previewLayer = PassioPlatformAISDK.shared.getPreviewLayer()
    super.init(frame: frame)
    setupPreview()
  }

  required init?(coder: NSCoder) {
    previewLayer = PassioPlatformAISDK.shared.getPreviewLayer()
    super.init(coder: coder)
    setupPreview()
  }

  public override func layoutSubviews() {
    NSLog("layoutSubviews")
    super.layoutSubviews()
    previewLayer?.frame = bounds
  }

  private func setupPreview() {
    NSLog("setup preview")
    guard let previewLayer = previewLayer else {
        return
    }
    previewLayer.videoGravity = .resizeAspectFill
    previewLayer.frame = frame
    layer.insertSublayer(previewLayer, at: 0)
  }
  
  deinit {
    PassioPlatformAISDK.shared.removeVidoeLayer()
  }
}
