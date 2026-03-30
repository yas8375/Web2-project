import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart-page.html'
})
export class CartPageComponent {
  response: any;
  errorMessage = '';

  constructor(
  private http: HttpClient,
  private router: Router
) {
  this.loadCart();
}

  loadCart(): void {
    this.http.get(`${environment.apiBaseUrl}/api/cart`, {
      withCredentials: true
    }).subscribe({
      next: (data) => this.response = data,
      error: (err) => this.errorMessage = err?.error?.message ?? 'Failed to load cart'
    });
  }

  updateQuantity(movieId: string, quantity: number): void {
    this.http.put(
      `${environment.apiBaseUrl}/api/cart/items/${movieId}`,
      { quantity },
      { withCredentials: true }
    ).subscribe({
      next: () => this.loadCart(),
      error: (err) => this.errorMessage = err?.error?.message ?? 'Failed to update cart item'
    });
  }

  removeItem(movieId: string): void {
    this.http.delete(
      `${environment.apiBaseUrl}/api/cart/items/${movieId}`,
      { withCredentials: true }
    ).subscribe({
      next: () => this.loadCart(),
      error: (err) => this.errorMessage = err?.error?.message ?? 'Failed to remove item'
    });
  }

  proceedToCheckout(): void {
  this.errorMessage = '';

  if (!this.response || this.response.totalItems === 0) {
    this.errorMessage = 'Add a movie before checkout.';
    return;
  }

  this.router.navigate(['/checkout']);
}
}