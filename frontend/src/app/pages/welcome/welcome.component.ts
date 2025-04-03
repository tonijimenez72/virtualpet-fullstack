import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Location } from '@models/location.enum';
import { Gadget } from '@models/gadget.enum';
import { Pet } from '@models/pet.model';
import { ErrorService } from '@services/error.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { decodeToken } from '@utils/auth.utils';
import { PaginatedResponse } from '@models/paginated-response.model';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
})
export class WelcomeComponent implements OnInit {
  pets: Pet[] = [];
  selectedPetId: string | null = localStorage.getItem('selectedPetId');
  newPet = { name: '', color: '', variety: '' };
  readonly apiUrl = 'http://localhost:8080';
  userRole: string | null = null;
  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;

  constructor(private http: HttpClient, private errorService: ErrorService) {}

  ngOnInit(): void {
    const decoded = decodeToken();
    this.userRole = decoded?.role ?? null;
    if (this.isLoggedIn) {
      console.log('🧠 Rol detectado en token:', this.userRole);
      this.fetchPets();
    }
  }

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('authToken');
  }

  get isAdmin(): boolean {
    return this.userRole === 'ADMIN';
  }

  fetchPets(): void {
    if (this.isAdmin) {
      const endpoint = '/pet/all';
      const params = new HttpParams()
        .set('page', this.currentPage.toString())
        .set('size', this.pageSize.toString());
      console.log(`[VirtualPet] Fetching from: ${endpoint} with params ${params.toString()}`);
      this.http.get<PaginatedResponse<Pet>>(`${this.apiUrl}${endpoint}`, { params }).subscribe({
        next: (response) => {
          this.pets = response.content.map(p => ({
            ...p,
            location: this.parseLocation(p.location)
          }));
          this.currentPage = response.number;
          this.totalPages = response.totalPages;
          console.log('[VirtualPet] Pets recibidos (paginated):', this.pets);
          if (this.pets.length > 0 && !this.selectedPetId) {
            this.selectPet(this.pets[0].id);
          }
        },
        error: (err) => this.handleHttpError(err, 'Failed to fetch pets'),
      });
    } else {
      const endpoint = '/pet/my';
      console.log(`[VirtualPet] Fetching from: ${endpoint}`);
      this.http.get<Pet[]>(`${this.apiUrl}${endpoint}`).subscribe({
        next: (pets) => {
          this.pets = pets.map(p => ({
            ...p,
            location: this.parseLocation(p.location)
          }));
          console.log('[VirtualPet] Pets recibidos:', this.pets);
          if (this.pets.length > 0 && !this.selectedPetId) {
            this.selectPet(this.pets[0].id);
          }
        },
        error: (err) => this.handleHttpError(err, 'Failed to fetch pets'),
      });
    }
  }

  createPet(): void {
    this.http.post<Pet>(`${this.apiUrl}/pet/create`, this.newPet).subscribe({
      next: (createdPet) => {
        createdPet.location = this.parseLocation(createdPet.location);
        this.pets.push(createdPet);
        this.newPet = { name: '', color: '', variety: '' };
        this.selectPet(createdPet.id);
      },
      error: (err) => this.handleHttpError(err, 'Failed to create pet'),
    });
  }

  selectPet(petId: string): void {
    this.selectedPetId = petId;
    localStorage.setItem('selectedPetId', petId);
  }

  deletePet(petId: string): void {
    this.http.delete(`${this.apiUrl}/pet/${petId}`).subscribe({
      next: () => {
        this.pets = this.pets.filter(p => p.id !== petId);
        if (this.selectedPetId === petId) {
          this.selectedPetId = null;
          localStorage.removeItem('selectedPetId');
        }
      },
      error: (err) => this.handleHttpError(err, 'Failed to delete pet'),
    });
  }

  petAction(action: 'play' | 'feed' | 'train', petId: string): void {
    this.http.put<Pet>(`${this.apiUrl}/pet/${petId}/activity?activity=${action.toUpperCase()}`, {}).subscribe({
      next: (updatedPet) => {
        updatedPet.location = this.parseLocation(updatedPet.location);
        this.updatePetLocally(updatedPet);
      },
      error: (err) => this.handleHttpError(err, `Failed to ${action} pet`)
    });
  }

  move(petId: string, location: Location): void {
    this.http.put<Pet>(`${this.apiUrl}/pet/${petId}/move?location=${location}`, {}).subscribe({
      next: (updatedPet) => {
        updatedPet.location = this.parseLocation(updatedPet.location);
        this.updatePetLocally(updatedPet);
      },
      error: (err) => this.handleHttpError(err, 'Failed to move pet'),
    });
  }

  updatePetLocally(updatedPet: Pet): void {
    const index = this.pets.findIndex(p => p.id === updatedPet.id);
    if (index !== -1) this.pets[index] = updatedPet;
  }

  getAvailableGadgetIcons(location: Location): string {
    const gadgetMap: Record<Location, Gadget[]> = {
      HOME: [Gadget.BONE, Gadget.TOY],
      PARK: [Gadget.BONE, Gadget.BALL],
      COUNTRY: [Gadget.STICK],
      BEACH: [Gadget.BALL]
    };
    const icons: Record<Gadget, string> = {
      BONE: '🦴',
      TOY: '🧸',
      BALL: '🥎',
      STICK: '🪄',
      NONE: ''
    };
    return gadgetMap[location].map(g => icons[g]).join(' ');
  }

  getOtherLocations(current: Location): Location[] {
    return Object.values(Location).filter(loc => loc !== current);
  }

  parseLocation(value: string): Location {
    return Location[value as keyof typeof Location];
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.fetchPets();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.fetchPets();
    }
  }

  private handleHttpError(error: HttpErrorResponse, fallback: string): void {
    const message = error.error?.message || fallback;
    this.errorService.setError({ status: error.status, message });
  }
}
