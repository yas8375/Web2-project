import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login-page.html'
})
export class LoginPageComponent {
  email = '';
  password = '';
  loading = false;
  message = '';

  constructor(private http: HttpClient) {}

  onLogin(): void {
    this.loading = true;
    this.message = '';
    this.http.post(`${environment.apiBaseUrl}/api/login`, {
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        this.loading = false;
        this.message = 'Login request sent.';
      },
      error: (err) => {
        this.loading = false;
        this.message = err?.error?.message ?? 'Login failed';
      }
    });
  }
}
