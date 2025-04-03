import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewPetComponent } from './new-pet.component';

describe('NewPetComponent', () => {
  let component: NewPetComponent;
  let fixture: ComponentFixture<NewPetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewPetComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewPetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
