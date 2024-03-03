import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoGoogleWallet.web.ts
// and on native platforms to ExpoGoogleWallet.ts
import ExpoGoogleWalletModule from './ExpoGoogleWalletModule';
import ExpoGoogleWalletView from './ExpoGoogleWalletView';
import { ChangeEventPayload, ExpoGoogleWalletViewProps } from './ExpoGoogleWallet.types';

// Get the native constant value.
export const PI = ExpoGoogleWalletModule.PI;

export function hello(): string {
  return ExpoGoogleWalletModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoGoogleWalletModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoGoogleWalletModule ?? NativeModulesProxy.ExpoGoogleWallet);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoGoogleWalletView, ExpoGoogleWalletViewProps, ChangeEventPayload };
