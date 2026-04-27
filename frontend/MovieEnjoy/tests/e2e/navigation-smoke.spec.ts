import { expect, test } from '@playwright/test';

test('top navigation routes to core pages without blank screen', async ({ page }) => {
  const navChecks = [
    {
      path: '/main',
      url: /\/main$/,
      heading: 'Find Something Worth Watching',
    },
    { path: '/movies', url: /\/movies$/, heading: 'Movie Catalog' },
    { path: '/cart', url: /\/cart$/, heading: 'Shopping Cart' },
    { path: '/login', url: /\/login$/, heading: 'Login' },
  ];

  for (const check of navChecks) {
    await page.goto(check.path);
    await expect(page).toHaveURL(check.url);
    await expect(page.getByRole('heading', { name: check.heading })).toBeVisible();
  }

  await page.goto('/browse/genres');
  await expect(page).toHaveURL(/\/browse\/genres$/);
  await expect(page.getByRole('heading', { name: 'Browse by Genres' })).toBeVisible();

  await page.goto('/browse/titles');
  await expect(page).toHaveURL(/\/browse\/titles$/);
  await expect(page.getByRole('heading', { name: 'Browse by Title' })).toBeVisible();
});
