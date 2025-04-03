import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AppError } from '../models/app-error.model';

@Injectable({ providedIn: 'root' })
export class ErrorService {
  private errorSubject = new BehaviorSubject<AppError | null>(null);
  error$ = this.errorSubject.asObservable();

  setError(error: AppError): void {
    this.errorSubject.next(error);
  }

  clearError(): void {
    this.errorSubject.next(null);
  }
}
