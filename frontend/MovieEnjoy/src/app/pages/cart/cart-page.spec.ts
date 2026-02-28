import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { CartPageComponent } from './cart-page';
import { environment } from '../../../environments/environment';

describe('Cart Phase 3 Expectations', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CartPageComponent, HttpClientTestingModule],
      providers: [provideRouter([])]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock?.verify();
  });

  it('should expose quantity update action (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(CartPageComponent);
    const component = fixture.componentInstance as any;

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/cart`);
    req.flush({ items: [] });

    expect(typeof component.updateQuantity).toBe('function');
  });

  it('should expose remove-item action (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(CartPageComponent);
    const component = fixture.componentInstance as any;

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/cart`);
    req.flush({ items: [] });

    expect(typeof component.removeItem).toBe('function');
  });
});
