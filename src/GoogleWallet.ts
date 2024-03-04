import ExpoGoogleWallet from "./ExpoGoogleWallet";

export function isWalletAvailable(): Promise<boolean> {
  return ExpoGoogleWallet.isWalletAvailable();
}

export function addWalletPass(json: string): Promise<number> {
  return ExpoGoogleWallet.savePass(json);
}
