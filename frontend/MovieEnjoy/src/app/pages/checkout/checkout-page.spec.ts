import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';

import { CheckoutPageComponent } from './checkout-page';
import { environment } from '../../../environments/environment';

describe('CheckoutPageComponent', () => {
  let httpMock: HttpTestingController;
  let navigateCalls: Array<{ commands: unknown[]; extras?: unknown }>;

  beforeEach(async () => {
    navigateCalls = [];

    await TestBed.configureTestingModule({
      imports: [CheckoutPageComponent, HttpClientTestingModule],
      providers: [
        {
          provide: Router,
          useValue: {
            navigate: (commands: unknown[], extras?: unknown) => {
              navigateCalls.push({ commands, extras });
              return Promise.resolve(true);
            }
          }
        }
      ]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should submit checkout payload and navigate on success', () => {
    const fixture = TestBed.createComponent(CheckoutPageComponent);
    const component = fixture.componentInstance;

    component.form = {
      firstName: 'Ali',
      lastName: 'Ahmed',
      cardNumber: '1234567890123456',
      expiration: '2030-12-01'
    };

    component.submit();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/checkout`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(component.form);
    req.flush({});

    expect(navigateCalls.length).toBe(1);
    expect((navigateCalls[0].commands as string[])[0]).toBe('/confirmation');
  });

  it('should set fallback message on checkout error', () => {
    const fixture = TestBed.createComponent(CheckoutPageComponent);
    const component = fixture.componentInstance;

    component.submit();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/checkout`);
    req.flush({}, { status: 501, statusText: 'Not Implemented' });

    expect(component.message).toBe('Not implemented yet');
  });

  it('should expose client-side card validation helper (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(CheckoutPageComponent);
    const component = fixture.componentInstance;

    expect(typeof (component as any).isValidCardNumber).toBe('function');
  });
});
