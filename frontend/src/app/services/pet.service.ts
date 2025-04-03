// src/app/services/pet.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pet } from '../models/pet.model';
import { Gadget } from '../models/gadget.enum';
import { Location } from '../models/location.enum';

@Injectable({ providedIn: 'root' })
export class PetService {
  private baseUrl = 'http://localhost:8080/pet';

  constructor(private http: HttpClient) {}

  getAllPets(): Observable<Pet[]> {
    return this.http.get<Pet[]>(`${this.baseUrl}/all`);
  }

  getMyPets(): Observable<Pet[]> {
    return this.http.get<Pet[]>(`${this.baseUrl}/my`);
  }

  getPetById(petId: string): Observable<Pet> {
    return this.http.get<Pet>(`${this.baseUrl}/${petId}`);
  }

  getPetsByUserId(userId: string): Observable<Pet[]> {
    return this.http.get<Pet[]>(`${this.baseUrl}/user/${userId}`);
  }

  createPet(petData: { name: string; variety: string; color: string }): Observable<Pet> {
    return this.http.post<Pet>(`${this.baseUrl}/create`, petData);
  }

  play(petId: string): Observable<Pet> {
    return this.http.put<Pet>(`${this.baseUrl}/${petId}/play`, {});
  }

  feed(petId: string): Observable<Pet> {
    return this.http.put<Pet>(`${this.baseUrl}/${petId}/feed`, {});
  }

  train(petId: string): Observable<Pet> {
    return this.http.put<Pet>(`${this.baseUrl}/${petId}/train`, {});
  }

  move(petId: string, location: Location): Observable<Pet> {
    return this.http.put<Pet>(`${this.baseUrl}/${petId}/move?location=${location}`, {});
  }

  getAllowedGadgets(location: Location): Observable<Gadget[]> {
    return this.http.get<Gadget[]>(`${this.baseUrl}/gadgets?location=${location}`);
  }
}
