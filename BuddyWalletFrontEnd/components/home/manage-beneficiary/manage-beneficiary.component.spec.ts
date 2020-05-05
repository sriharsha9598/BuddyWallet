import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageBeneficiaryComponent } from './manage-beneficiary.component';

describe('ManageBeneficiaryComponent', () => {
  let component: ManageBeneficiaryComponent;
  let fixture: ComponentFixture<ManageBeneficiaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageBeneficiaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageBeneficiaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
