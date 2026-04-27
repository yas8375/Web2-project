import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

import { MainPageComponent } from './main-page';

describe('MainPage Phase 3 Expectations', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MainPageComponent],
      providers: [
        {
          provide: Router,
          useValue: { navigate: () => Promise.resolve(true) },
        },
      ],
    }).compileComponents();
  });

  it('should expose quick title search action (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(MainPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.searchByTitle).toBe('function');
  });

  it('should expose browse by genre action from home (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(MainPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.browseByGenre).toBe('function');
  });
});
