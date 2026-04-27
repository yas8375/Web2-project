import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { environment } from '../../../environments/environment';
import { CartStateService } from '../../cart-state.service';

@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart-page.html',
  styleUrl: './cart-page.css',
})
export class CartPageComponent implements OnInit {
  private readonly destroyRef = inject(DestroyRef);

  response: any;
  errorMessage = '';
  successMessage = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private cartState: CartStateService,
  ) {}

  ngOnInit(): void {
    this.loadCart();

    this.cartState.changes$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.loadCart());

    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((event) => {
        const navigation = event as NavigationEnd;
        if (navigation.urlAfterRedirects.startsWith('/cart')) {
          this.loadCart();
        }
      });
  }

  loadCart(): void {
    this.http
      .get(`${environment.apiBaseUrl}/api/cart`, {
        withCredentials: true,
      })
      .subscribe({
        next: (data) => {
          this.response = data;
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.errorMessage = err?.error?.message ?? 'Failed to load cart';
          this.cdr.detectChanges();
        },
      });
  }

  updateQuantity(movieId: string, quantity: number): void {
    this.errorMessage = '';
    this.successMessage = 'Quantity updated';
    this.cdr.detectChanges();

    setTimeout(() => {
      this.successMessage = '';
      this.cdr.detectChanges();
    }, 2500);

    const item = this.response?.items?.find(
      (entry: any) => entry.movieId === movieId,
    );
    if (item) {
      item.quantity = quantity;
    }

    this.http
      .put(
        `${environment.apiBaseUrl}/api/cart/items/${movieId}`,
        { quantity },
        { withCredentials: true },
      )
      .subscribe({
        next: () => {
          this.loadCart();
        },
        error: (err) => {
          this.errorMessage =
            err?.error?.message ?? 'Failed to update quantity';
          this.cdr.detectChanges();
        },
      });
  }

  removeItem(movieId: string): void {
    this.http
      .delete(`${environment.apiBaseUrl}/api/cart/items/${movieId}`, {
        withCredentials: true,
      })
      .subscribe({
        next: () => {
          this.loadCart();
        },
        error: (err) => {
          this.errorMessage = err?.error?.message ?? 'Failed to remove item';
          this.cdr.detectChanges();
        },
      });
  }

  proceedToCheckout(): void {
    this.router.navigate(['/checkout']);
  }
}
