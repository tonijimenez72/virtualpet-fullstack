import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { ErrorService } from './error.service';
import { AppError } from '../models/app-error.model';

interface AuthResponse {
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/auth';

  constructor(
    private http: HttpClient,
    private errorService: ErrorService,
    private router: Router
  ) {}

  login(credentials: { email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(res => {
        this.handleAuthSuccess(res);
        setTimeout(() => this.router.navigate(['/welcome']), 100);
      }),
      catchError(error => this.handleError(error, 'Unknown error during login'))
    );
  }

  register(data: { email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data).pipe(
      tap(res => {
        this.handleAuthSuccess(res);
        setTimeout(() => this.router.navigate(['/welcome']), 100);
      }),
      catchError(error => this.handleError(error, 'Registration failed'))
    );
  }

  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('email');
    localStorage.removeItem('username');
    localStorage.removeItem('selectedPetId');
  }

private handleAuthSuccess(res: AuthResponse): void {
  if (res?.token) {
    localStorage.setItem('authToken', res.token);
    const decoded = this.decodeToken(res.token);

    if (decoded?.role) {
      localStorage.setItem('userRole', decoded.role);
    }

    if (decoded?.sub) {
      localStorage.setItem('email', decoded.sub);
      localStorage.setItem('username', decoded.sub.split('@')[0]);
    }
  }
}

private decodeToken(token: string): any {
  try {
    const payload = token.split('.')[1];
    const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error('❌ Error decoding token:', e);
    return null;
  }
}


  private handleError(error: any, fallbackMessage: string): Observable<never> {
    const backendError: AppError = error.error || {
      status: error.status || 0,
      message: error.message || fallbackMessage,
    };
    this.errorService.setError(backendError);
    return throwError(() => new Error(backendError.message));
  }
}
