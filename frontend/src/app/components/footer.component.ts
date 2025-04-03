import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { ErrorService } from '../services/error.service';
import { AppError } from '../models/app-error.model';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {
  error$: Observable<AppError | null>;

  constructor(private errorService: ErrorService) {
    this.error$ = this.errorService.error$;
  }

  clearError(): void {
    this.errorService.clearError();
  }
}
