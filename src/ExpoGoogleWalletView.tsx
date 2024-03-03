import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoGoogleWalletViewProps } from './ExpoGoogleWallet.types';

const NativeView: React.ComponentType<ExpoGoogleWalletViewProps> =
  requireNativeViewManager('ExpoGoogleWallet');

export default function ExpoGoogleWalletView(props: ExpoGoogleWalletViewProps) {
  return <NativeView {...props} />;
}
