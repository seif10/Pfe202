import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmptyThreadComponent } from './empty-thread.component';

describe('EmptyThreadComponent', () => {
  let component: EmptyThreadComponent;
  let fixture: ComponentFixture<EmptyThreadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmptyThreadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmptyThreadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
