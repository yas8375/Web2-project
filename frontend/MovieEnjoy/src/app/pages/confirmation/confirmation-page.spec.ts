import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { ConfirmationPageComponent } from './confirmation-page';

describe('Confirmation Phase 3 Expectations', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmationPageComponent],
      providers: [provideRouter([])]
    }).compileComponents();
  });

  it('should expose purchased items summary model (Phase 4 expectation)', () => {
    history.replaceState({}, '', location.href);
    const fixture = TestBed.createComponent(ConfirmationPageComponent);
    const component = fixture.componentInstance as any;

    expect(Array.isArray(component.items)).toBe(true);
  });

  it('should expose order id in confirmation state (Phase 4 expectation)', () => {
    history.replaceState({}, '', location.href);
    const fixture = TestBed.createComponent(ConfirmationPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.orderId).toBe('string');
    expect(component.orderId?.length).toBeGreaterThan(0);
  });
});
