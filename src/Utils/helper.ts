/**
 * @author Louay Sleman
 * @contact louayakram12@hotmail.com
 * @linkedin https://www.linkedin.com/in/louay-sleman
 * @version 2.0.0
 * @website https://louaysleman.com
 * @copyright Copyright (c) 2024 Louay Sleman. All rights reserved.
 */
import { ErrorMessages } from '../constants';
import { DeviceEventEmitter } from 'react-native';

export const handleError = <T>(
  error: unknown,
  errorMessage: string,
  fallback: T,
  additionalInfo?: Record<string, unknown>
): T => {
  if (__DEV__) {
    console.error(
      errorMessage,
      additionalInfo ? `\nAdditional Info: ${additionalInfo}` : '',
      '\nError:',
      error
    );
  }
  return fallback;
};

export const safeJsonParse = <T>(
  jsonString: string | null | undefined,
  fallback: T
): T => {
  if (!jsonString) return fallback;
  try {
    return JSON.parse(jsonString) as T;
  } catch {
    return handleError(
      new Error(ErrorMessages.JSON_PARSE_ERROR),
      ErrorMessages.JSON_PARSE_ERROR,
      fallback
    );
  }
};

export const createEventListener = (
  eventName: string,
  callback: (app: string) => void
): (() => void) => {
  const subscription = DeviceEventEmitter.addListener(eventName, callback);
  return () => subscription.remove();
};
