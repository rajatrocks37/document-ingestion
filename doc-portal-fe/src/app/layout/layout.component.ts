
import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { SidebarComponent } from './sidebar/sidebar.component';

@Component({
  standalone: true,
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css'],
  imports: [CommonModule, HeaderComponent, FooterComponent, SidebarComponent, RouterOutlet]
})
export class LayoutComponent {
  isSidebarCollapsed = signal(false);

  toggleSidebar() {
    this.isSidebarCollapsed.update((v) => !v);
  }
}
