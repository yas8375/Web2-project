import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthStateService } from './auth-state.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authState = inject(AuthStateService);

  const token = authState.getToken();

  const isApiRequest = req.url.includes('/api/');
  const isAuthRequest =
    req.url.includes('/api/login') ||
    req.url.includes('/api/signup') ||
    req.url.includes('/api/signup/check-email');

  let clonedReq = req;

  if (isApiRequest && token && !isAuthRequest) {
    clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(clonedReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        authState.clear();
        router.navigate(['/login'], {
          queryParams: {
            returnUrl: router.url,
            message: 'Please sign in to continue.'
          }
        });
      }

      return throwError(() => error);
    })
  );
};
