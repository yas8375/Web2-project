import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { CartPageComponent } from './cart-page';
import { environment } from '../../../environments/environment';

describe('CartPageComponent', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CartPageComponent, HttpClientTestingModule],
      providers: [provideRouter([])]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should load cart on component creation', () => {
    const fixture = TestBed.createComponent(CartPageComponent);
    const component = fixture.componentInstance;

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/cart`);
    expect(req.request.method).toBe('GET');
    req.flush({ items: [] });

    expect(component.response).toEqual({ items: [] });
  });

  it('should set fallback message when cart endpoint fails', () => {
    const fixture = TestBed.createComponent(CartPageComponent);
    const component = fixture.componentInstance;

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/cart`);
    req.flush({}, { status: 501, statusText: 'Not Implemented' });

    expect(component.errorMessage).toBe('Not implemented yet');
  });

  it('should expose quantity update and item removal handlers (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(CartPageComponent);
    const component = fixture.componentInstance;

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/cart`);
    req.flush({ items: [] });

    expect(typeof (component as any).updateQuantity).toBe('function');
    expect(typeof (component as any).removeItem).toBe('function');
  });
});
