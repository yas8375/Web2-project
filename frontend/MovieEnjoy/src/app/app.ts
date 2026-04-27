import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { environment } from '../environments/environment';
import { AuthStateService } from './auth-state.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  authReady = true;

  constructor(
    private http: HttpClient,
    private router: Router,
    private authState: AuthStateService,
  ) {}

  get isLoggedIn(): boolean {
    return this.authState.isLoggedIn();
  }

  get isLoginRoute(): boolean {
    return this.router.url.startsWith('/login');
  }

  logout(): void {
    this.http
      .post(
        `${environment.apiBaseUrl}/api/logout`,
        {},
        { withCredentials: true },
      )
      .subscribe({
        next: () => {
          this.authState.clear();
          this.router.navigate(['/login']);
        },
        error: () => {
          this.authState.clear();
          this.router.navigate(['/login']);
        },
      });
  }
}
