import { expect, test } from '@playwright/test';

test('top navigation routes to core pages without blank screen', async ({ page }) => {
  const navChecks = [
    {
      link: 'Home',
      url: /\/main$/,
      heading: 'Find Something Worth Watching',
    },
    { link: 'Movies', url: /\/movies$/, heading: 'Movie Catalog' },
    { link: 'Cart', url: /\/cart$/, heading: 'Shopping Cart' },
    { link: 'Login', url: /\/login$/, heading: 'Login' },
  ];

  await page.goto('/main');

  for (const check of navChecks) {
    await page.getByRole('link', { name: check.link }).click();
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
