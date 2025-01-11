import { TurboModuleRegistry } from 'react-native';
import type { LauncherKitSpec } from './Interfaces/turboModule';

export default TurboModuleRegistry.getEnforcing<LauncherKitSpec>('LauncherKit');
