import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';

export const roleGuard = (expectedRoles: string[]): CanActivateFn => {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);
    const userRole = authService.getUserRole(); // e.g. "ADMIN", "EDITOR"

    if (expectedRoles.includes(userRole)) {
      return true;
    }

    return router.createUrlTree(['/dashboard']);
  };
};
