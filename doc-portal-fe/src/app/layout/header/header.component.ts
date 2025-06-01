import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../shared/auth.service';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  imports: [CommonModule]
})
export class HeaderComponent {
  @Output() toggleSidebar = new EventEmitter<void>();

  constructor(public auth: AuthService, private router: Router) { }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}