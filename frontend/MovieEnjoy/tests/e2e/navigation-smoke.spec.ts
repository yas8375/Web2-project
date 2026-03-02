import { expect, test } from '@playwright/test';

test('top navigation routes to core pages without blank screen', async ({ page }) => {
  const navChecks = [
    { link: 'Home', url: /\/main$/, heading: 'Main Page' },
    { link: 'Movies', url: /\/movies$/, heading: 'Movie List' },
    { link: 'Genres', url: /\/browse\/genres$/, heading: 'Browse by Genres' },
    { link: 'Titles', url: /\/browse\/titles$/, heading: 'Browse by Title' },
    { link: 'Cart', url: /\/cart$/, heading: 'Shopping Cart' },
    { link: 'Login', url: /\/login$/, heading: 'Login' },
  ];

  await page.goto('/main');

  for (const check of navChecks) {
    await page.getByRole('link', { name: check.link }).click();
    await expect(page).toHaveURL(check.url);
    await expect(page.getByRole('heading', { name: check.heading })).toBeVisible();
  }
});
