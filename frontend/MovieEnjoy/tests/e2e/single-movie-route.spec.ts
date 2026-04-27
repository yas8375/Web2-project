import { expect, test } from '@playwright/test';

test('open single movie route shows details shell and back navigation', async ({ page }) => {
  await page.goto('/movies/tt1000001');

  await expect(page).toHaveURL(/\/movies\/tt1000001$/);
  await expect(page.getByText('Movie Details')).toBeVisible();
  await expect(page.getByRole('link', { name: 'Back to catalog' })).toBeVisible();

  await page.getByRole('link', { name: 'Back to catalog' }).click();
  await expect(page).toHaveURL(/\/movies$/);
  await expect(page.getByRole('heading', { name: 'Movie Catalog' })).toBeVisible();
});
