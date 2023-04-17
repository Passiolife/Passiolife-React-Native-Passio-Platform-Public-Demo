//
//  PassioCameraViewManager.m
//  MobileAI
//
//  Created by Winoto, Basuki on 4/13/23.
//

#import <Foundation/Foundation.h>
#import <MobileAI-Swift.h>
#import <React/RCTViewManager.h>

@interface PassioCameraViewManager: RCTViewManager
@end

@implementation PassioCameraViewManager

RCT_EXPORT_MODULE()

- (UIView *) view {
  PassioCameraView *view = [[PassioCameraView alloc] initWithFrame:CGRectZero];
  return view;
}

@end
