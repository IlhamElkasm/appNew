import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterSecretaireComponent } from './register-secretaire.component';

describe('RegisterSecretaireComponent', () => {
  let component: RegisterSecretaireComponent;
  let fixture: ComponentFixture<RegisterSecretaireComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegisterSecretaireComponent]
    });
    fixture = TestBed.createComponent(RegisterSecretaireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
