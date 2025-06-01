import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthService {
  token = signal<string | null>(null);
  readonly baseUrl = 'http://localhost:9000/api/auth/login';
  readonly apiUrl = 'http://localhost:9000';
  constructor(private http: HttpClient) { }

  getToken(): string | null {
    if (!!this.token()) {
      return this.token();
    }
    const token = localStorage.getItem('token');
    if (token) {
      this.token.set(token);
    }
    return this.token();
  }

  login(username: string, password: string) {
    const body = {
      username: username,
      password: password
    };

    return this.http.post<any>(this.baseUrl, body);
  }

  logout() {
    this.token.set(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  register(data: { username: string, password: string }) {
    return this.http.post(`${this.apiUrl}/api/auth/register`, data);
  }

  getCurrentUser(): any {
    return JSON.parse(localStorage.getItem('user') || '{}');
  }

  isAuthenticated(): boolean {
    console.log('Checking authentication status...' + !!this.token());
    return !!this.getToken();
  }

  getUserRole(): string {
    const user = this.getCurrentUser();
    return user?.role || '';
  }
}
