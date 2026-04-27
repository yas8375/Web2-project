import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CartStateService {
  private readonly changesSubject = new Subject<void>();
  readonly changes$ = this.changesSubject.asObservable();

  notifyChanged(): void {
    this.changesSubject.next();
  }
}
