function getApiBaseUrl(): string {
  // Avoid referencing `window` during SSR/build steps.
  const loc = typeof window !== 'undefined' ? window.location : undefined;
  const isHttps = loc?.protocol === 'https:';

  // Default to local dev URLs. For deployed environments, this should be set per deployment.
  return isHttps ? 'https://localhost:9443' : 'http://localhost:8081';
}

export const environment = {
  production: true,
  apiBaseUrl: getApiBaseUrl(),
};
