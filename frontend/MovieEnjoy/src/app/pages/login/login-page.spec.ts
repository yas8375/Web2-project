import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { LoginPageComponent } from './login-page';

describe('Login Phase 3 Expectations', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginPageComponent, HttpClientTestingModule],
      providers: [provideRouter([])],
    }).compileComponents();
  });

  it('should expose email policy validator (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(LoginPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.isAllowedEmail).toBe('function');
  });

  it('should expose password strength validator (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(LoginPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.isStrongPassword).toBe('function');
  });
});
