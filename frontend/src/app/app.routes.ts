import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },


  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.component')
      .then(m => m.RegisterComponent)
  },

  {
    path: 'welcome',
    loadComponent: () => import('./pages/welcome/welcome.component')
      .then(m => m.WelcomeComponent)
  },

  {
    path: 'new-pet',
    loadComponent: () => import('./pages/new-pet/new-pet.component')
      .then(m => m.NewPetComponent)
  }
];
