import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-signup-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './signup-page.html',
  styleUrl: './signup-page.css',
})
export class SignupPageComponent {
  firstName = '';
  lastName = '';
  address = '';
  creditCardId = '';
  expiration = '';
  email = '';
  password = '';
  confirmPassword = '';
  showPassword = false;
  showConfirmPassword = false;
  loading = false;
  message = '';
  success = false;

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  onSignup(): void {
    if (!this.isFormValid()) {
      return;
    }

    this.loading = true;
    this.message = '';

    this.http
      .post(
        `${environment.apiBaseUrl}/api/signup`,
        {
          firstName: this.firstName.trim(),
          lastName: this.lastName.trim(),
          address: this.address.trim(),
          creditCardId: this.creditCardId.replace(/[\s-]+/g, ''),
          expiration: this.expiration.trim(),
          email: this.email.trim(),
          password: this.password,
          confirmPassword: this.confirmPassword,
        },
        { withCredentials: true },
      )
      .subscribe({
        next: () => {
          this.success = true;
          this.loading = false;
          this.message =
            'Account created successfully. Redirecting to sign in...';

          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 900);
        },
        error: (err) => {
          this.success = false;
          this.loading = false;
          this.message = err?.error?.message ?? 'Signup failed';
        },
      });
  }

  private isFormValid(): boolean {
    if (
      !this.firstName.trim() ||
      !this.lastName.trim() ||
      !this.address.trim()
    ) {
      this.success = false;
      this.message = 'Please complete your personal information.';
      return false;
    }

    const normalizedCardId = this.creditCardId.replace(/[\s-]+/g, '');
    if (!/^\d{1,20}$/.test(normalizedCardId)) {
      this.success = false;
      this.message = 'Please enter a valid card number.';
      return false;
    }

    if (!/^\d{4}-\d{2}-\d{2}$/.test(this.expiration.trim())) {
      this.success = false;
      this.message = 'Please enter the expiration date as YYYY-MM-DD.';
      return false;
    }

    if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(this.email.trim())) {
      this.success = false;
      this.message = 'Please enter a valid email address.';
      return false;
    }

    if (this.password.trim().length < 6) {
      this.success = false;
      this.message = 'Password must be at least 6 characters.';
      return false;
    }

    if (this.password !== this.confirmPassword) {
      this.success = false;
      this.message = 'Passwords do not match.';
      return false;
    }

    return true;
  }
}
