import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-confirmation-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './confirmation-page.html',
  styleUrl: './confirmation-page.css',
})
export class ConfirmationPageComponent {
  private readonly state: any = this.readConfirmationState();

  success = this.state?.success ?? false;
  message = this.resolveMessage(this.state?.message);
  items = this.normalizeItems(this.state?.items);
  orderId = this.resolveOrderId(this.state?.orderId);

  private readConfirmationState(): any {
    const hs = history.state ?? {};
    if (
      hs &&
      (hs.success !== undefined || hs.message || hs.orderId || hs.items)
    )
      return hs;

    try {
      const raw = sessionStorage.getItem('checkout_confirmation');
      if (!raw) return {};
      return JSON.parse(raw);
    } catch {
      return {};
    }
  }

  private resolveMessage(value: unknown): string {
    if (typeof value === 'string' && value.trim().length > 0)
      return value.trim();
    return this.success
      ? 'Your payment was completed successfully.'
      : 'Payment was not completed. Please try again.';
  }

  private normalizeItems(
    value: unknown,
  ): Array<{ title: string; quantity: number }> {
    if (!Array.isArray(value)) return [];
    return value.map((item: any, index: number) => {
      const rawTitle = typeof item?.title === 'string' ? item.title.trim() : '';
      const movieId = item?.movieId;
      const title =
        rawTitle.length > 0
          ? rawTitle
          : movieId !== undefined && movieId !== null
            ? `Movie #${movieId}`
            : `Item ${index + 1}`;

      const quantityValue = Number(item?.quantity);
      const quantity =
        Number.isFinite(quantityValue) && quantityValue > 0
          ? Math.floor(quantityValue)
          : 1;

      return { title, quantity };
    });
  }

  private resolveOrderId(value: unknown): string {
    if (typeof value === 'string' && value.trim().length > 0)
      return value.trim();
    return 'pending';
  }
}
