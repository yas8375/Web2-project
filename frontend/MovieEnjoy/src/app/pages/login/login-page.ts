import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login-page.html',
  styleUrl: './login-page.css'
})
export class LoginPageComponent {
  email = '';
  password = '';
  loading = false;
  message = '';
  messageType: 'success' | 'error' | 'info' = 'info';

  constructor(private http: HttpClient, private router: Router) {}

  isAllowedEmail(email: string = this.email): boolean {
    const value = (email ?? '').trim();
    // Keep policy simple for now: must look like an email.
    return /^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(value);
  }

  isStrongPassword(password: string = this.password): boolean {
    const value = (password ?? '').trim();
    // Keep policy simple for now: at least 4 chars (dataset uses short passwords).
    return value.length >= 4;
  }

  onLogin(): void {
    if (!this.isAllowedEmail() || !this.isStrongPassword()) {
      this.message = 'Please enter a valid email and password.';
      this.messageType = 'error';
      return;
    }

    this.loading = true;
    this.message = '';
    this.http.post(`${environment.apiBaseUrl}/api/login`, {
      email: this.email,
      password: this.password
    }, { withCredentials: true }).subscribe({
      next: () => {
        this.loading = false;
        this.message = 'Login successful.';
        this.messageType = 'success';
        localStorage.setItem('customer_email', this.email.trim());
        this.router.navigate(['/main']);
      },
      error: (err) => {
        this.loading = false;
        this.message = err?.error?.message ?? 'Login failed';
        this.messageType = 'error';
      }
    });
  }
}
