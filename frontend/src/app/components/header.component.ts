import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ErrorService } from '../services/error.service';
import { AppError } from '../models/app-error.model'; // Asegúrate de que el path es correcto

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  email = '';
  password = '';
  username: string | null = null;
  selectedPetName: string | null = null;

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('authToken');
  }

  constructor(
    private http: HttpClient,
    private router: Router,
    private errorService: ErrorService
  ) {
    const token = localStorage.getItem('authToken');
    const user = localStorage.getItem('username');
    if (token && user) {
      this.username = user;
    }
  }

  ngOnInit(): void {
    const petName = localStorage.getItem('selectedPetName');
    if (petName) {
      this.selectedPetName = petName;
    }
  }

  login(): void {
    this.clearError();
    this.http.post<any>('http://localhost:8080/auth/login', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: (res) => {
        localStorage.setItem('authToken', res.token);
        const username = this.email.split('@')[0];
        localStorage.setItem('username', username);
        this.username = username;
        this.router.navigate(['/welcome']);
      },
      error: (error: HttpErrorResponse) => {
        const status = error.status ;
        const message = error.error?.message;
        this.errorService.setError({ status, message });
      }
    });
  }

  register(): void {
    this.clearError();
    this.http.post<any>('http://localhost:8080/auth/register', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        this.router.navigate(['/welcome']);
      },
      error: (error: HttpErrorResponse) => {
        const status = error.status;
        const message = error.error?.message;
        this.errorService.setError({ status, message });
      }
    });
  }

  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('username');
    localStorage.removeItem('selectedPetName');
    this.username = null;
    this.selectedPetName = null;
    this.router.navigate(['/']);
    this.clearError();
  }

  private clearError(): void {
    this.errorService.clearError();
  }
}
