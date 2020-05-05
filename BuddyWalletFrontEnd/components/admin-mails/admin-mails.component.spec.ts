import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminMailsComponent } from './admin-mails.component';

describe('AdminMailsComponent', () => {
  let component: AdminMailsComponent;
  let fixture: ComponentFixture<AdminMailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminMailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminMailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
