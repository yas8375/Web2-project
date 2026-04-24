import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { timeout } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-checkout-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './checkout-page.html',
  styleUrl: './checkout-page.css'
})
export class CheckoutPageComponent {
  form = {
    firstName: '',
    lastName: '',
    cardNumber: '',
    expiration: ''
  };
  message = '';
  isSubmitting = false;

  constructor(private http: HttpClient, private router: Router, private cdr: ChangeDetectorRef) {}

  isValidCardNumber(cardNumber: string): boolean {
    // For this project, correctness is validated by backend lookup in `creditcards` table.
    // Accept 1-20 digits after removing spaces/hyphens.
    const digits = (cardNumber ?? '').replace(/[\s-]+/g, '');
    return /^\d{1,20}$/.test(digits);
  }

  isValidExpiration(expiration: string): boolean {
    // Expected format: YYYY-MM-DD (also accept YYYY/MM/DD because seed data uses slashes)
    // Validate it as a real calendar date only (no "expired" logic by design).
    const raw = (expiration ?? '').trim();
    const match = raw.match(/^(\d{4})[-/](\d{2})[-/](\d{2})$/);
    if (!match) return false;

    const year = Number(match[1]);
    const month = Number(match[2]);
    const day = Number(match[3]);
    if (!Number.isInteger(year) || !Number.isInteger(month) || !Number.isInteger(day)) return false;
    if (month < 1 || month > 12) return false;
    if (day < 1 || day > 31) return false;

    const date = new Date(Date.UTC(year, month - 1, day));
    if (Number.isNaN(date.getTime())) return false;

    return date.getUTCFullYear() === year
      && date.getUTCMonth() === month - 1
      && date.getUTCDate() === day;
  }

  submit(): void {
    this.message = '';
    this.isSubmitting = true;

    if (!this.isValidCardNumber(this.form.cardNumber)) {
      this.message = 'Invalid card number';
      this.isSubmitting = false;
      return;
    }
    if (!this.isValidExpiration(this.form.expiration)) {
      this.message = 'Invalid date';
      this.isSubmitting = false;
      return;
    }

    this.http
      .post<any>(`${environment.apiBaseUrl}/api/checkout`, this.form, { withCredentials: true })
      .pipe(timeout(10000))
      .subscribe({
      next: (res) => {
        // في حال أرجع الباك إند 200 OK لكن العملية فشلت
        if (res && res.success === false) {
          this.message = 'Invalid date';
          this.isSubmitting = false;
          this.cdr.detectChanges();
          return;
        }

        const confirmationState = {
          success: res?.success ?? true,
          message: res?.message ?? 'Checkout complete',
          items: res?.items
        };

        // Preserve confirmation info across refresh/navigation.
        try {
          sessionStorage.setItem('checkout_confirmation', JSON.stringify(confirmationState));
        } catch {
          // ignore storage failures
        }

        // Show message even if navigation fails.
        this.message = confirmationState.message;
        this.router.navigate(['/confirmation'], { state: confirmationState }).catch(() => {
          this.message = `${confirmationState.message} (Could not navigate to confirmation page)`;
        }).finally(() => {
          this.isSubmitting = false;
        });
      },
      error: (err) => {
        if (err?.name === 'TimeoutError') {
          this.message = `Request timed out. Is the backend running at ${environment.apiBaseUrl}?`;
        } else {
          this.message = err?.error?.message ?? 'Request failed';
        }
        this.isSubmitting = false;
        this.cdr.detectChanges(); // إجبار تحديث الواجهة لإيقاف التعليق وعرض الرسالة
      }
    });
  }
}
