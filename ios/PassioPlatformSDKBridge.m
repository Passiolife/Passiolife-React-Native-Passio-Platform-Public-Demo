//
//  PassioPlatformSDKBridge.m
//  MobileAI
//
//  Created by Winoto, Basuki on 4/12/23.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(PassioPlatformSDKBridge, RCTEventEmitter)
RCT_EXTERN_METHOD(configure:(NSString *)key
                  projectID:(NSString *)projectID
                  debugMode:(NSInteger)debugMode
                  autoUpdate:(BOOL)autoUpdate
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(startDetection)
RCT_EXTERN_METHOD(stopDetection)
RCT_EXTERN_METHOD(supportedEvents)

@end
