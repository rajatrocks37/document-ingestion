import { HttpInterceptorFn, HttpErrorResponse, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../shared/auth.service';


export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const router = inject(Router);
    const authService = inject(AuthService);

    // URLs that don't require authentication
    const excludedUrls: string[] = [
        '/api/auth/login',
        '/api/auth/register',
        '/api/auth/refresh',
        '/assets'
    ];

    // Check if the request URL should be excluded from auth
    const isExcluded = excludedUrls.some(url => req.url.includes(url));

    if (isExcluded) {
        return next(req);
    }

    // Get the auth token
    const token = authService.getToken();

    // Clone the request and add the authorization header
    if (token) {
        req = addToken(req, token);
    }

    // Handle the request
    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            if (error.status === 401) {
                // Token might be expired, clear it and redirect to login
                authService.logout();
                router.navigate(['/login']);
            } else if (error.status === 403) {
                // Handle 403 Forbidden
                // router.navigate(['/forbidden']);
            }

            return throwError(() => error);
        })
    );

    function addToken(request: HttpRequest<unknown>, token: string): HttpRequest<unknown> {
        return request.clone({
            setHeaders: {
                'Authorization': `Bearer ${token}`
            }
        });
    }
};