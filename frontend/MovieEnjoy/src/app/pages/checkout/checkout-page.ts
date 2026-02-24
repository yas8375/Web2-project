import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-checkout-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './checkout-page.html'
})
export class CheckoutPageComponent {
  form = {
    firstName: '',
    lastName: '',
    cardNumber: '',
    expiration: ''
  };
  message = '';

  constructor(private http: HttpClient, private router: Router) {}

  submit(): void {
    this.http.post(`${environment.apiBaseUrl}/api/checkout`, this.form).subscribe({
      next: () => {
        this.router.navigate(['/confirmation'], { state: { success: true, message: 'Checkout complete' } });
      },
      error: (err) => {
        this.message = err?.error?.message ?? 'Not implemented yet';
      }
    });
  }
}
