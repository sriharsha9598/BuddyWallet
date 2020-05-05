import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEditAcntComponent } from './admin-edit-acnt.component';

describe('AdminEditAcntComponent', () => {
  let component: AdminEditAcntComponent;
  let fixture: ComponentFixture<AdminEditAcntComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminEditAcntComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminEditAcntComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
