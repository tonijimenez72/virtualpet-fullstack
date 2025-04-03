import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-new-pet',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, HttpClientModule],
  templateUrl: './new-pet.component.html',
  styleUrls: ['./new-pet.component.css']
})
export class NewPetComponent {
  pet = {
    name: '',
    variety: '',
    color: ''
  };

  constructor(private http: HttpClient, private router: Router) {}

  createPet(): void {
    const token = localStorage.getItem('authToken');

    if (!token) {
      alert('You must be logged in to create a pet.');
      this.router.navigate(['/login']);
      return;
    }

    this.http.post('http://localhost:8080/pet/create', this.pet, { // 👈 URL corregida
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: () => {
        alert('Pet created successfully!');
        this.router.navigate(['/welcome']);
      },
      error: (err: any) => {
        alert('Failed to create pet');
        console.error(err);
      }
    });
  }
}
