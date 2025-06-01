import { CommonModule } from "@angular/common";
import { HttpClient } from "@angular/common/http";
import { Component, signal } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { AuthService } from "../shared/auth.service";

@Component({
  standalone: true,
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css'],
  imports: [CommonModule, FormsModule]
})
export class UserManagementComponent {
  users = signal<{ username: string; role: string }[]>([]);
  newUser = {
    username: '',
    password: '',
    role: ''
  };
  roles = ['ADMIN', 'EDITOR', 'VIEWER'];

  constructor(private http: HttpClient, private auth: AuthService) {
    this.loadUsers();
  }

  loadUsers() {
    this.http.get<any[]>('http://localhost:9000/api/users', {
      headers: { Authorization: `Bearer ${this.auth.token()}` }
    }).subscribe({
      next: (data) => {
        this.users.set(data);
      },
      error: (err) => {
        console.error('Failed to load users', err);
      }
    });
  }

  createUser() {
    if (!this.newUser.username || !this.newUser.password || !this.newUser.role) return;

    this.http.post('http://localhost:9000/api/users', this.newUser, {
      headers: { Authorization: `Bearer ${this.auth.token()}` }
    }).subscribe({
      next: () => {
        this.loadUsers();
        this.newUser = { username: '', password: '', role: '' };
      },
      error: (err) => {
        console.error('Error creating user', err);
      }
    });
  }

  deleteUser(username: string) {
    if (!confirm(`Are you sure you want to delete user "${username}"?`)) return;

    this.http.delete(`http://localhost:9000/api/users/${username}`, {
      headers: { Authorization: `Bearer ${this.auth.token()}` }
    }).subscribe({
      next: () => this.loadUsers(),
      error: (err) => console.error('Error deleting user', err)
    });
  }
}