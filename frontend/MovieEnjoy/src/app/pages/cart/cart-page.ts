import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart-page.html'
})
export class CartPageComponent {
  response: any;
  errorMessage = '';

  constructor(private http: HttpClient) {
    this.loadCart();
  }

  loadCart(): void {
    this.http.get('http://localhost:8081/api/cart').subscribe({
      next: (data) => this.response = data,
      error: (err) => this.errorMessage = err?.error?.message ?? 'Not implemented yet'
    });
  }
}
