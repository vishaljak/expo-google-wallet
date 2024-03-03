import * as React from 'react';

import { ExpoGoogleWalletViewProps } from './ExpoGoogleWallet.types';

export default function ExpoGoogleWalletView(props: ExpoGoogleWalletViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
