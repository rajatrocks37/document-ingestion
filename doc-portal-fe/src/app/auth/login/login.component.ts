import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../shared/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';


@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onLogin(): void {
    if (this.loginForm.invalid) return;

    const { username, password } = this.loginForm.value;

    this.authService.login(username, password).subscribe({
      next: (res) => {
        localStorage.setItem('user', JSON.stringify(res));
        this.authService.token.set(res.token);
        localStorage.setItem('token', res.token);
        // Redirect based on role
        switch (res?.role) {
          case 'ADMIN':
            this.router.navigate(['/users']);
            break;
          case 'EDITOR':
            this.router.navigate(['/upload']);
            break;
          case 'VIEWER':
            this.router.navigate(['/qa']);
            break;
          default:
            this.router.navigate(['/']);
        }
      },
      error: (err) => {
        this.errorMessage = 'Invalid username or password.';
      }
    });
  }
}