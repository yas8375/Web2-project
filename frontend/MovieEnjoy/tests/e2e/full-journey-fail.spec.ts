import { expect, test } from '@playwright/test';

test('full journey succeeds from login to checkout confirmation (expected to fail until backend is implemented)', async ({
  page,
}) => {
  await page.goto('/login');
  await page.getByPlaceholder('Email').fill('user@example.com');
  await page.getByPlaceholder('Password').fill('password123');
  await page.getByRole('button', { name: 'Login' }).click();

  await page.getByRole('link', { name: 'Cart' }).click();
  await expect(page).toHaveURL(/\/cart$/);

  await page.getByRole('link', { name: 'Proceed to Checkout' }).click();
  await expect(page).toHaveURL(/\/checkout$/);

  await page.getByPlaceholder('First name').fill('Raghad');
  await page.getByPlaceholder('Last name').fill('Alyousfy');
  await page.getByPlaceholder('Card number').fill('1234567890123456');
  await page.getByPlaceholder('YYYY-MM-DD').fill('2030-12-31');
  await page.getByRole('button', { name: 'Pay' }).click();

  // This should fail now because POST /api/checkout returns 501 Not Implemented.
  await expect(page).toHaveURL(/\/confirmation$/);
  await expect(page.getByText('Payment successful')).toBeVisible();
});
