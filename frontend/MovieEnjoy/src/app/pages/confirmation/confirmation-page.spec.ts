import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { ConfirmationPageComponent } from './confirmation-page';

describe('ConfirmationPageComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmationPageComponent],
      providers: [provideRouter([])]
    }).compileComponents();
  });

  it('should read success and message from navigation state', () => {
    history.replaceState({ success: true, message: 'Checkout complete' }, '', location.href);

    const fixture = TestBed.createComponent(ConfirmationPageComponent);
    const component = fixture.componentInstance;

    expect(component.success).toBe(true);
    expect(component.message).toBe('Checkout complete');
  });

  it('should show default values when state is missing', () => {
    history.replaceState({}, '', location.href);

    const fixture = TestBed.createComponent(ConfirmationPageComponent);
    const component = fixture.componentInstance;

    expect(component.success).toBe(false);
    expect(component.message).toBe('');
  });

  it('should expose order summary model for purchased items (Phase 4 expectation)', () => {
    history.replaceState({}, '', location.href);

    const fixture = TestBed.createComponent(ConfirmationPageComponent);
    const component = fixture.componentInstance;

    expect(Array.isArray((component as any).items)).toBe(true);
  });
});
