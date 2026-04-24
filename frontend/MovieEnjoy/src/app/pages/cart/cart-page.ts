import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart-page.html',
  styleUrl: './cart-page.css'
})
export class CartPageComponent {
  response: any;
  errorMessage = '';
  successMessage = '';
  private successTimer: any;

  constructor(
  private http: HttpClient,
  private router: Router,
  private cdr: ChangeDetectorRef,

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
this.errorMessage = '';
this.successMessage = 'Quantity updated';
 this.cdr.detectChanges();
setTimeout(() => {
    this.successMessage = '';
    this.cdr.detectChanges();
  }, 2500);
 
  const item = this.response.items.find((i: any) => i.movieId === movieId);
  if (item) {
    item.quantity = quantity;
  }

  this.http.put(
    `${environment.apiBaseUrl}/api/cart/items/${movieId}`,
    { quantity },
    { withCredentials: true }
  ).subscribe({
    error: () => {
      this.loadCart(); // fallback لو فشل
    }
  });
}

  removeItem(movieId: string): void {
this.errorMessage = '';
this.successMessage = 'Movie removed from cart.';
 this.cdr.detectChanges();
setTimeout(() => {
    this.successMessage = '';
    this.cdr.detectChanges();
  }, 2500);

  this.response.items = this.response.items.filter(
    (i: any) => i.movieId !== movieId
  );

  this.response.totalItems--;

  this.http.delete(
    `${environment.apiBaseUrl}/api/cart/items/${movieId}`,
    { withCredentials: true }
  ).subscribe({
    error: () => {
      this.loadCart(); // fallback
    }
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
