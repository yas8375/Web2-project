import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { LoginPageComponent } from './login-page';
import { environment } from '../../../environments/environment';

describe('LoginPageComponent', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginPageComponent, HttpClientTestingModule]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(LoginPageComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });

  it('should post login payload and set success message', () => {
    const fixture = TestBed.createComponent(LoginPageComponent);
    const component = fixture.componentInstance;

    component.email = 'user@example.com';
    component.password = 'secret';
    component.onLogin();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'user@example.com', password: 'secret' });
    req.flush({});

    expect(component.loading).toBe(false);
    expect(component.message).toBe('Login request sent.');
  });

  it('should show backend error message when login fails', () => {
    const fixture = TestBed.createComponent(LoginPageComponent);
    const component = fixture.componentInstance;

    component.onLogin();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/login`);
    req.flush({ message: 'Invalid credentials' }, { status: 401, statusText: 'Unauthorized' });

    expect(component.loading).toBe(false);
    expect(component.message).toBe('Invalid credentials');
  });

  it('should expose client-side email policy validator (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(LoginPageComponent);
    const component = fixture.componentInstance;

    expect(typeof (component as any).isAllowedEmail).toBe('function');
  });
});
