function getApiBaseUrl(): string {
  // Avoid referencing `window` during SSR/build steps.
  const loc = typeof window !== 'undefined' ? window.location : undefined;
  const isHttps = loc?.protocol === 'https:';

  // Dev convention:
  // - Backend HTTP:  http://localhost:8081
  // - Backend HTTPS: https://localhost:9443 (8443 is also supported, but 9443 avoids common conflicts)
  return isHttps ? 'https://localhost:9443' : 'http://localhost:8081';
}

export const environment = {
  production: false,
  apiBaseUrl: getApiBaseUrl(),
};
