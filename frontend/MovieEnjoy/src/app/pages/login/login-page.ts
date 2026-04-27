import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthStateService } from '../../auth-state.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login-page.html',
  styleUrl: './login-page.css'
})
export class LoginPageComponent {
  email = '';
  password = '';
  loading = false;
  message = '';
  messageType: 'success' | 'error' | 'info' = 'info';
  private readonly returnUrl: string;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private authState: AuthStateService
  ) {
    this.message = this.route.snapshot.queryParamMap.get('message') ?? '';
    this.returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') ?? '/main';
  }

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
        this.authState.setLoggedIn(this.email);
        this.router.navigateByUrl(this.returnUrl);
      },
      error: (err) => {
        this.loading = false;
        this.message = err?.error?.message ?? 'Login failed';
        this.messageType = 'error';
      }
    });
  }
}
