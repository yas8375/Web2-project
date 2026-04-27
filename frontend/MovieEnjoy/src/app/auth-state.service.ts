import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private readonly customerEmailKey = 'customer_email';

  isLoggedIn(): boolean {
    return (
      (localStorage.getItem(this.customerEmailKey) ?? '').trim().length > 0
    );
  }

  setLoggedIn(email: string): void {
    localStorage.setItem(this.customerEmailKey, email.trim());
  }

  clear(): void {
    localStorage.removeItem(this.customerEmailKey);
  }
}
