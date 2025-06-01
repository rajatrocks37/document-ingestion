// import { CommonModule } from '@angular/common';
// import { HttpClient } from '@angular/common/http';
// import { Component } from '@angular/core';
// import { FormsModule } from '@angular/forms';
// import { Router } from '@angular/router';

// @Component({
//   selector: 'app-register',
//   templateUrl: './register.component.html',
//   styleUrl: './register.component.css',
//   standalone: true,
//   imports: [CommonModule, FormsModule]
// })
// export class RegisterComponent {
//   username = '';
//   password = '';
//   role = 'VIEWER';

//   constructor(private http: HttpClient, private router: Router) { }

//   signup() {
//     const body = { username: this.username, password: this.password, role: this.role };
//     this.http.post('http://localhost:8080/api/auth/signup', body).subscribe(() => {
//       alert('Signup successful');
//       this.router.navigateByUrl('/login');
//     });
//   }
// }


import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../shared/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  passwordMatchValidator(group: FormGroup): { [key: string]: boolean } | null {
    return group.get('password')?.value === group.get('confirmPassword')?.value
      ? null : { mismatch: true };
  }

  onRegister(): void {
    if (this.registerForm.invalid) return;

    const { username, password } = this.registerForm.value;

    this.authService.register({ username, password }).subscribe({
      next: () => {
        this.successMessage = 'Registration successful. Redirecting to login...';
        this.errorMessage = '';

        setTimeout(() => this.router.navigate(['/login']), 5000);
      },
      error: () => {
        this.errorMessage = 'Registration failed. Try again.';
        this.successMessage = '';
      }
    });
  }
}
