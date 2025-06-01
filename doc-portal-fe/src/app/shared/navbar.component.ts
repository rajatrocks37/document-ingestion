import { Component } from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { AuthService } from "./auth.service";

@Component({
  standalone: true,
  selector: 'app-navbar',
  template: `
    <nav class="bg-blue-600 p-4 text-white flex justify-between">
      <div>Doc Portal</div>
      <div>
        <a routerLink="/" class="px-2">Dashboard</a>
        <a routerLink="/qa" class="px-2">Q&A</a>
        <a (click)="logout()" class="px-2 cursor-pointer">Logout</a>
      </div>
    </nav>
  `,
  imports: [RouterModule]
})
export class NavbarComponent {
  constructor(private auth: AuthService, private router: Router) {}

  logout() {
    this.auth.logout();
    this.router.navigateByUrl('/login');
  }
}
