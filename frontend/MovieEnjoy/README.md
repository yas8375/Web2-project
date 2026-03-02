# MovieEnjoy

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 21.1.3.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Running k6 stress tests (Phase 3)

These tests target backend endpoint `GET /api/movies`.

1. Start backend app on `http://localhost:8081`.
2. Make sure `k6` is installed and available in your terminal.
3. Run one of:

```bash
npm run perf:k6:movies:smoke
npm run perf:k6:movies:stress
```

To test another base URL:

```bash
k6 run -e BASE_URL=http://localhost:8082 tests/performance/movies-stress.js
```

Current pass thresholds in `tests/performance/movies-stress.js`:

- `http_req_failed < 1%`
- `p95(http_req_duration) < 1000ms`

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
