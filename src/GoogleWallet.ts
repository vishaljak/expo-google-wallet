import ExpoGoogleWallet from "./ExpoGoogleWallet";

export function isGoogleWalletAvailable(): Promise<boolean> {
  return ExpoGoogleWallet.isGoogleWalletAvailable();
}

export function addWalletPass(json: string): Promise<number> {
  return ExpoGoogleWallet.savePass(json);
}
