import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormationDetail2Component } from './formation-detail2.component';

describe('FormationDetail2Component', () => {
  let component: FormationDetail2Component;
  let fixture: ComponentFixture<FormationDetail2Component>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormationDetail2Component]
    });
    fixture = TestBed.createComponent(FormationDetail2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
