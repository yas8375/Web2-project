import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { App } from './app';

describe('App Phase 3 Expectations', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideRouter([])],
    }).compileComponents();
  });

  it('should expose auth-ready state for protected routes (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(App);
    const component = fixture.componentInstance as any;

    expect(typeof component.authReady).toBe('boolean');
  });

  it('should expose logout action in root component (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(App);
    const component = fixture.componentInstance as any;

    expect(typeof component.logout).toBe('function');
  });
});
