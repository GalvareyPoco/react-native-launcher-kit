/**
 * LauncherKit Helper Module
 *
 * @author Louay Sleman
 * @contact louayakram12@hotmail.com
 * @linkedin https://www.linkedin.com/in/louay-sleman
 * @version 2.1.0
 * @website https://louaysleman.com
 * @copyright Copyright (c) 2024 Louay Sleman. All rights reserved.
 */

import { Defaults, ErrorMessages } from '../constants';
import type {
  LauncherKitHelperProps,
  LaunchParams,
} from '../Interfaces/helper';
import { initializeModule } from '../Utils/moduleInitializer';
import { handleError } from '../Utils/helper';
import type { BatteryStatus } from '../Interfaces/battery';

// Initialize the LauncherKit module
const LauncherKit = initializeModule();

/**
 * A helper object with utility functions for launching apps and interacting
 * with the launcher on Android devices.
 */
const LauncherKitHelper: LauncherKitHelperProps = {
  /**
   * Launches an application with optional intent parameters.
   */
  launchApplication: (bundleId: string, params?: LaunchParams): boolean => {
    if (!bundleId) {
      return handleError(
        new Error(ErrorMessages.BUNDLE_ID_REQUIRED),
        ErrorMessages.BUNDLE_ID_REQUIRED,
        false
      );
    }

    try {
      LauncherKit.launchApplication(bundleId, params);
      return true;
    } catch (error) {
      return handleError(
        error,
        ErrorMessages.LAUNCH_APP_ERROR(bundleId),
        false,
        { params }
      );
    }
  },

  goToSettings: (): void => {
    try {
      LauncherKit.goToSettings();
    } catch (error) {
      handleError(error, 'Failed to open settings', undefined);
    }
  },

  checkIfPackageInstalled: async (bundleId: string): Promise<boolean> => {
    try {
      return await LauncherKit.isPackageInstalled(bundleId);
    } catch (error) {
      return handleError(
        error,
        `Failed to check if package is installed: ${bundleId}`,
        false
      );
    }
  },

  getDefaultLauncherPackageName: async (): Promise<string> => {
    try {
      return await LauncherKit.getDefaultLauncherPackageName();
    } catch (error) {
      return handleError(
        error,
        ErrorMessages.DEFAULT_LAUNCHER_ERROR,
        Defaults.EMPTY_STRING
      );
    }
  },

  openAlarmApp: (): boolean => {
    try {
      LauncherKit.openAlarmApp();
      return true;
    } catch (error) {
      return handleError(error, 'Failed to open alarm app', false);
    }
  },

  getBatteryStatus: async (): Promise<BatteryStatus> => {
    try {
      return await LauncherKit.getBatteryStatus();
    } catch (error) {
      return handleError(
        error,
        'Failed to get battery status',
        Defaults.BATTERY_STATUS
      );
    }
  },

  openSetDefaultLauncher: async (): Promise<boolean> => {
    try {
      return await LauncherKit.openSetDefaultLauncher();
    } catch (error) {
      throw handleError(
        error,
        ErrorMessages.SET_DEFAULT_LAUNCHER_ERROR,
        error as Error
      );
    }
  },
};

export default LauncherKitHelper;
