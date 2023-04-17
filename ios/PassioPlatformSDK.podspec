Pod::Spec.new do |s|
    s.name                  = 'PassioPlatformSDK'
    s.version               = '1.0.0'
    s.summary               = 'Passio Platform SDK'
    s.authors               = 'support@passiolife.com'
    s.license               = { :type => 'Passio' }
    s.homepage              = 'https://www.passio.ai'
    s.platform              = :ios
    s.vendored_frameworks   = 'PassioPlatformSDK.xcframework'
    s.source                = { :git => 'https://github.com/passio' }
end
