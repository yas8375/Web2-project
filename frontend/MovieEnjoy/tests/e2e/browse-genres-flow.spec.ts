import { expect, test } from '@playwright/test';

test('browse by genres page opens and shows the main heading', async ({ page }) => {
  await page.goto('/browse/genres');

  await expect(page).toHaveURL(/\/browse\/genres$/);
  await expect(page.getByRole('heading', { name: 'Browse by Genres' })).toBeVisible();
});
