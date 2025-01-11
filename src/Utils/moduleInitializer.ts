import { TurboModuleRegistry, NativeModules } from 'react-native';
import { AppConfig, ErrorMessages } from '../constants';
import type { LauncherKitSpec } from '../Interfaces/turboModule';

export const initializeModule = (): LauncherKitSpec => {
  if ((global as any).__turboModuleProxy) {
    return TurboModuleRegistry.getEnforcing<LauncherKitSpec>(
      AppConfig.MODULE_NAME
    );
  }

  if (NativeModules.LauncherKit) {
    return NativeModules.LauncherKit;
  }

  return new Proxy({} as LauncherKitSpec, {
    get() {
      throw new Error(ErrorMessages.LINKING_ERROR);
    },
  });
};
