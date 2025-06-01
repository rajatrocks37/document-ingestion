import { HttpInterceptorFn } from '@angular/common/http';
import { tap } from 'rxjs';

export const loggingInterceptor: HttpInterceptorFn = (req, next) => {
  console.log('Outgoing request:', req.method, req.url);
  
  const started = Date.now();
  
  return next(req).pipe(
    tap({
      next: (event) => {
        console.log(`Request took ${Date.now() - started}ms`);
      },
      error: (error) => {
        console.error('Request failed:', error);
      }
    })
  );
};