import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';

import { CheckoutPageComponent } from './checkout-page';

describe('Checkout Phase 3 Expectations', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CheckoutPageComponent, HttpClientTestingModule],
      providers: [
        {
          provide: Router,
          useValue: { navigate: () => Promise.resolve(true) },
        },
      ],
    }).compileComponents();
  });

  it('should expose card-number validator helper (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(CheckoutPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.isValidCardNumber).toBe('function');
  });

  it('should expose expiration-date validator helper (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(CheckoutPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.isValidExpiration).toBe('function');
  });
});
