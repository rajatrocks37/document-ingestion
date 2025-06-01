import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../shared/auth.service';

interface MenuItem {
  label: string;
  route: string;
  role?: string[]; // optional array of allowed roles
}

@Component({
  standalone: true,
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  imports: [CommonModule, RouterModule]
})
export class SidebarComponent {
  @Input() collapsed = false;
  filteredMenuItems: MenuItem[] = [];

  menuItems: MenuItem[] = [
    { label: 'Dashboard', route: '/dashboard' },
    { label: 'Upload Documents', route: '/upload' },
    { label: 'Manage Documents', route: '/manage-docs', role: ['ADMIN', 'EDITOR'] },
    { label: 'Q&A Interface', route: '/qa' },
    { label: 'User Management', route: '/users', role: ['ADMIN'] },
  ];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    const userRole = this.authService.getUserRole();
    this.filteredMenuItems = this.menuItems.filter(item => {
      return !item.role || item.role.includes(userRole);
    });
  }
}
