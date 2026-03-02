import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<1000'],
  },
  scenarios: {
    ramping_load: {
      executor: 'ramping-vus',
      startVUs: 5,
      stages: [
        { duration: '30s', target: 20 },
        { duration: '1m', target: 50 },
        { duration: '30s', target: 0 },
      ],
      gracefulRampDown: '10s',
    },
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';
const MOVIES_ENDPOINT = `${BASE_URL}/api/movies?page=1&size=50`;

export default function () {
  const response = http.get(MOVIES_ENDPOINT, {
    headers: { Accept: 'application/json' },
    timeout: '30s',
  });

  check(response, {
    'status is 200': (r) => r.status === 200,
    'response is json-like': (r) => (r.headers['Content-Type'] || '').includes('application/json'),
  });

  sleep(1);
}
