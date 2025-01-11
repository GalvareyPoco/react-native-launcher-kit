import type { TurboModule } from 'react-native';
import type { LaunchParams } from './helper';

export interface LauncherKitSpec extends TurboModule {
  getApps(includeVersion: boolean, includeAccentColor: boolean): Promise<any>;
  launchApplication(packageName: string, params?: LaunchParams): void;
  isPackageInstalled(packageName: string): Promise<boolean>;
  getDefaultLauncherPackageName(): Promise<string>;
  setAsDefaultLauncher(): void;
  getBatteryStatus(): Promise<any>;
  goToSettings(): void;
  openAlarmApp(): void;
  openSetDefaultLauncher(): Promise<boolean>;
  startListeningForAppInstallations(): void;
  stopListeningForAppInstallations(): void;
  startListeningForAppRemovals(): void;
  stopListeningForAppRemovals(): void;
}
