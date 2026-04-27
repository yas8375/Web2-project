import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private readonly customerEmailKey = 'customer_email';

  isLoggedIn(): boolean {
    if (!this.hasLocalStorage()) return false;
    return (
      (localStorage.getItem(this.customerEmailKey) ?? '').trim().length > 0
    );
  }

  setLoggedIn(email: string): void {
    if (!this.hasLocalStorage()) return;
    localStorage.setItem(this.customerEmailKey, email.trim());
  }

  clear(): void {
    if (!this.hasLocalStorage()) return;
    localStorage.removeItem(this.customerEmailKey);
  }

  private hasLocalStorage(): boolean {
    return typeof localStorage !== 'undefined';
  }
}
