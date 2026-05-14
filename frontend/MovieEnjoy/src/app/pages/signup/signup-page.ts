import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TimeoutError } from 'rxjs';
import { finalize, timeout } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-signup-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './signup-page.html',
  styleUrl: './signup-page.css',
})
export class SignupPageComponent implements OnInit {
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
  readonly passwordRules =
    'Use at least 8 characters, including uppercase, lowercase, and a number.';

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.resetForm();
  }

  onSignup(): void {
    if (!this.isFormValid()) {
      return;
    }

    this.loading = true;
    this.message = '';
    this.success = false;

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
      .pipe(
        timeout(8000),
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        }),
      )
      .subscribe({
        next: () => {
          this.success = true;
          this.message =
            'Account created successfully. Redirecting to sign in...';
          this.cdr.detectChanges();

          setTimeout(() => {
            this.router.navigate(['/login'], {
              queryParams: {
                email: this.email.trim(),
                password: this.password,
                message: 'Account created successfully. Please sign in.'
              }
            });
          }, 900);
        },
        error: (err) => {
          this.success = false;
          this.message =
            err instanceof TimeoutError
              ? 'Signup request timed out. Please make sure the backend is running.'
              : err?.error?.message ?? err?.message ?? 'Signup failed';
          this.cdr.detectChanges();
        },
      });
  }

  private isFormValid(): boolean {
    const safeFirstName = this.firstName.trim();
    const safeLastName = this.lastName.trim();
    const safeAddress = this.address.trim();
    const safeEmail = this.email.trim();
    const safeExpiration = this.expiration.trim();

    if (!safeFirstName || !safeLastName || !safeAddress) {
      this.success = false;
      this.message = 'Please complete your personal information.';
      return false;
    }

    if (!/^[A-Za-z][A-Za-z\s'-]{1,49}$/.test(safeFirstName)) {
      this.success = false;
      this.message = 'First name must contain letters only and be at least 2 characters.';
      return false;
    }

    if (!/^[A-Za-z][A-Za-z\s'-]{1,49}$/.test(safeLastName)) {
      this.success = false;
      this.message = 'Last name must contain letters only and be at least 2 characters.';
      return false;
    }

    if (safeAddress.length < 5) {
      this.success = false;
      this.message = 'Please enter a complete address.';
      return false;
    }

    const normalizedCardId = this.creditCardId.replace(/[\s-]+/g, '');
    if (!/^\d+$/.test(normalizedCardId)) {
      this.success = false;
      this.message = 'Credit card number must contain numbers only.';
      return false;
    }

    if (!/^\d{4}-\d{2}-\d{2}$/.test(safeExpiration)) {
      this.success = false;
      this.message = 'Please enter the expiration date as YYYY-MM-DD.';
      return false;
    }

    const expirationDate = new Date(`${safeExpiration}T00:00:00`);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (Number.isNaN(expirationDate.getTime()) || expirationDate < today) {
      this.success = false;
      this.message = 'Expiration date must be today or later.';
      return false;
    }

    if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(safeEmail)) {
      this.success = false;
      this.message = 'Please enter a valid email address.';
      return false;
    }

    if (this.password.length < 8) {
      this.success = false;
      this.message = 'Password must be at least 8 characters.';
      return false;
    }

    if (!/[A-Z]/.test(this.password) || !/[a-z]/.test(this.password) || !/\d/.test(this.password)) {
      this.success = false;
      this.message = 'Password must include uppercase, lowercase, and a number.';
      return false;
    }

    if (this.password !== this.confirmPassword) {
      this.success = false;
      this.message = 'Passwords do not match.';
      return false;
    }

    return true;
  }

  private resetForm(): void {
    this.firstName = '';
    this.lastName = '';
    this.address = '';
    this.creditCardId = '';
    this.expiration = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
    this.message = '';
    this.success = false;
    this.loading = false;
  }
}
