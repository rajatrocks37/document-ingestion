import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './auth/auth.guard';
import { roleGuard } from './auth/role.guard';

// import { LoginComponent } from './auth//login/login.component';
// import { RegisterComponent } from './auth/register/register.component';
// import { DashboardComponent } from './dashboard/dashboard.component';
// import { UserManagementComponent } from './user-management/user-management.component';
// import { UploadComponent } from './documents/upload/upload.component';
// import { ManageComponent } from './document/manage.component';
// import { IngestionComponent } from './ingestion/ingestion.component';
// import { QAComponent } from './qa/qa.component';

// export const routes: Routes = [
//   { path: '', component: DashboardComponent },
//   { path: 'login', component: LoginComponent },
//   { path: 'register', component: RegisterComponent },
//   { path: 'users', component: UserManagementComponent },
//   { path: 'upload', component: UploadComponent },
//   { path: 'manage-docs', component: ManageComponent },
//   { path: 'ingestion', component: IngestionComponent },
//   { path: 'qa', component: QAComponent }
// ];



export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent), canActivate: [authGuard] },
      { path: 'manage-docs', loadComponent: () => import('./documents/manage/manage.component').then(m => m.ManageComponent), canActivate: [authGuard] },
      { path: 'upload', loadComponent: () => import('./documents/upload/upload.component').then(m => m.UploadComponent), canActivate: [authGuard] },
      { path: 'users', loadComponent: () => import('./user-management/user-management.component').then(m => m.UserManagementComponent) },
      { path: 'ingestion', loadComponent: () => import('./ingestion/ingestion.component').then(m => m.IngestionComponent), canActivate: [authGuard] },
      { path: 'qa', loadComponent: () => import('./ask/ask.component').then(m => m.AskComponent), canActivate: [authGuard] },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ]
  }
];
