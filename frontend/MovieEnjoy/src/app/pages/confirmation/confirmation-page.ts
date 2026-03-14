import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-confirmation-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './confirmation-page.html'
})
export class ConfirmationPageComponent {
  private readonly state: any = this.readConfirmationState();

  success = this.state?.success ?? false;
  message = this.state?.message ?? 'No confirmation details found. Please checkout again.';
  items: any[] = Array.isArray(this.state?.items) ? this.state.items : [];
  orderId: string = this.coerceOrderId(this.state?.orderId);

  private readConfirmationState(): any {
    const hs = history.state ?? {};
    if (hs && (hs.success !== undefined || hs.message || hs.orderId || hs.items)) return hs;

    try {
      const raw = sessionStorage.getItem('checkout_confirmation');
      if (!raw) return {};
      return JSON.parse(raw);
    } catch {
      return {};
    }
  }

  private coerceOrderId(value: unknown): string {
    if (typeof value === 'string' && value.trim().length > 0) return value.trim();
    return this.generateOrderId();
  }

  private generateOrderId(): string {
    const c: any = (globalThis as any).crypto;
    if (c?.randomUUID) return c.randomUUID();
    return `order_${Date.now().toString(36)}_${Math.random().toString(36).slice(2, 10)}`;
  }
}
