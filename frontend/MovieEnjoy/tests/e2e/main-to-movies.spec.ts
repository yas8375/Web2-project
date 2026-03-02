import { expect, test } from '@playwright/test';

test('main page browse button navigates to movies and shows table', async ({ page }) => {
  await page.goto('/main');

  await expect(page.getByRole('heading', { name: 'Main Page' })).toBeVisible();
  await page.getByRole('button', { name: 'Browse Movies' }).click();

  await expect(page).toHaveURL(/\/movies$/);
  await expect(page.getByRole('heading', { name: 'Movie List' })).toBeVisible();
  await expect(page.locator('tbody tr')).toHaveCount(4);
  await expect(page.getByText('Alpha Movie')).toBeVisible();
});
