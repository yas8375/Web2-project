import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private readonly customerEmailKey = 'customer_email';
  private readonly authTokenKey = 'auth_token';

  isLoggedIn(): boolean {
    if (!this.hasLocalStorage()) return false;
    const token = (localStorage.getItem(this.authTokenKey) ?? '').trim();
    return token.length > 0;
  }

  setLoggedIn(email: string, token?: string): void {
    if (!this.hasLocalStorage()) return;
    localStorage.setItem(this.customerEmailKey, email.trim());
    const cleanToken = (token ?? '').trim();
    if (cleanToken.length > 0) {
      localStorage.setItem(this.authTokenKey, cleanToken);
    }
  }

  getToken(): string | null {
    if (!this.hasLocalStorage()) return null;
    const token = (localStorage.getItem(this.authTokenKey) ?? '').trim();
    return token.length > 0 ? token : null;
  }

  clear(): void {
    if (!this.hasLocalStorage()) return;
    localStorage.removeItem(this.customerEmailKey);
    localStorage.removeItem(this.authTokenKey);
  }

  private hasLocalStorage(): boolean {
    return typeof localStorage !== 'undefined';
  }
}
