import { expect, test } from '@playwright/test';

test('checkout page keeps labels and reports mock backend payment rejection', async ({ page }) => {
  await page.goto('/checkout');

  await expect(page).toHaveURL(/\/checkout$/);
  await expect(page.getByRole('heading', { name: 'Checkout' })).toBeVisible();
  await expect(page.getByLabel('First Name')).toBeVisible();
  await expect(page.getByLabel('Last Name')).toBeVisible();
  await expect(page.getByLabel('Card Number')).toBeVisible();
  await expect(page.getByLabel('Expiration Date')).toBeVisible();

  await page.getByLabel('First Name').fill('Ali');
  await page.getByLabel('Last Name').fill('Ahmed');
  await page.getByLabel('Card Number').fill('1234567890123456');
  await page.getByLabel('Expiration Date').fill('2030-12-31');
  await page.getByRole('button', { name: 'Pay Now' }).click();

  await expect(page).toHaveURL(/\/checkout$/);
  await expect(page.getByRole('alert')).toContainText(
    'Checkout is unavailable while the mock backend profile is active',
  );
});
