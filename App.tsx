import React, {useEffect, useState} from 'react';
import {
  Button,
  HostComponent,
  NativeEventEmitter,
  NativeModules,
  PermissionsAndroid,
  Platform,
  requireNativeComponent,
  Text,
  View,
  ViewProps,
} from 'react-native';

const PassioSDK = NativeModules.PassioPlatformSDKBridge;
const PassioCameraView = requireNativeComponent(
  'PassioCameraView',
) as HostComponent<ViewProps>;

function App(): JSX.Element {
  const [sdkStatus, setSdkStatus] = useState<string>();
  const [isDetecting, setIsDetecting] = useState(false);
  const [candidate, setCandidate] = useState<string>();

  useEffect(() => {
    const requestCameraPermission = async () => {
      if (Platform.OS === 'ios') {
        return;
      }
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.CAMERA,
          {
            title: 'Cool Photo App Camera Permission',
            message:
              'Cool Photo App needs access to your camera ' +
              'so you can take awesome pictures.',
            buttonNeutral: 'Ask Me Later',
            buttonNegative: 'Cancel',
            buttonPositive: 'OK',
          },
        );
        if (granted === PermissionsAndroid.RESULTS.GRANTED) {
          console.log('You can use the camera');
        } else {
          console.log('Camera permission denied');
        }
      } catch (err) {
        console.warn(err);
      }
    };
    requestCameraPermission();
  }, []);

  useEffect(() => {
    PassioSDK.configure(
      'license-key', // your license key here
      'project-id', // your projectID here
      1, // set debugMode here
      true, // set autoUpdate as true
    ).then((result: string) => {
      console.log({result});
      setSdkStatus(result);
    });
    const PassioSDKListener = new NativeEventEmitter(PassioSDK);
    PassioSDKListener.addListener('onDetectionCandidates', setCandidate);
    return () => PassioSDK.removeListeners();
  }, []);

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      {sdkStatus === 'isReadyForDetection' ? (
        <>
          <PassioCameraView style={{flex: 1, width: '100%'}} />
          <View
            style={{
              position: 'absolute',
              width: '100%',
              justifyContent: 'center',
              alignItems: 'center',
              bottom: 100,
            }}>
            <Button
              title={!isDetecting ? 'Start' : 'Stop'}
              onPress={() => {
                console.log({PassioSDK});
                if (!isDetecting) {
                  PassioSDK.startDetection();
                } else {
                  PassioSDK.stopDetection();
                }
                setIsDetecting(!isDetecting);
              }}
            />
            <Text>{`Candidate: ${candidate}`}</Text>
          </View>
        </>
      ) : (
        <Text>RN Passio Platform SDK</Text>
      )}
    </View>
  );
}

export default App;
