/**
 * @author Louay Sleman
 * @contact louayakram12@hotmail.com
 * @linkedin https://www.linkedin.com/in/louay-sleman
 * @version 2.1.0
 * @website https://louaysleman.com
 * @copyright Copyright (c) 2024 Louay Sleman. All rights reserved.
 */
import { DeviceEventEmitter } from 'react-native';
import type {
  AppDetail,
  AppEventCallback,
  GetAppsOptions,
  InstalledApps,
} from '../Interfaces/InstalledApps';
import { AppConfig, AppEvents } from '../constants';
import {
  createEventListener,
  handleError,
  safeJsonParse,
} from '../Utils/helper';
import { initializeModule } from '../Utils/moduleInitializer';

// Initialize the LauncherKit LauncherKit
const LauncherKit = initializeModule();

/**
 * Object containing functions to retrieve information about installed apps.
 */
const installedApps: InstalledApps = {
  async getApps(options?: GetAppsOptions): Promise<AppDetail[]> {
    try {
      const { includeVersion, includeAccentColor } = {
        ...AppConfig.DEFAULT_OPTIONS,
        ...options,
      };
      const apps = await LauncherKit.getApps(
        includeVersion,
        includeAccentColor
      );
      return safeJsonParse<AppDetail[]>(apps, []);
    } catch (error) {
      return handleError(error, '[]', []);
    }
  },

  async getSortedApps(options?: GetAppsOptions): Promise<AppDetail[]> {
    try {
      const apps = await this.getApps(options);
      return apps.sort((a, b) =>
        (a.label?.toLowerCase() ?? '').localeCompare(
          b.label?.toLowerCase() ?? ''
        )
      );
    } catch (error) {
      return handleError(error, '[]', []);
    }
  },

  startListeningForAppInstallations(callback: AppEventCallback): void {
    const handler = (app: string) =>
      callback(safeJsonParse<AppDetail>(app, {} as AppDetail));
    createEventListener(AppEvents.APP_INSTALLED, handler);
    LauncherKit.startListeningForAppInstallations();
  },

  stopListeningForAppInstallations(): void {
    DeviceEventEmitter.removeAllListeners(AppEvents.APP_INSTALLED);
    LauncherKit.stopListeningForAppInstallations();
  },

  startListeningForAppRemovals(callback: (packageName: string) => void): void {
    createEventListener(AppEvents.APP_REMOVED, callback);
    LauncherKit.startListeningForAppRemovals();
  },

  stopListeningForAppRemovals(): void {
    DeviceEventEmitter.removeAllListeners(AppEvents.APP_REMOVED);
    LauncherKit.stopListeningForAppRemovals();
  },
};

export default installedApps;
