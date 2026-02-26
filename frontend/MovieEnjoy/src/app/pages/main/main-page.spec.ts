import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

import { MainPageComponent } from './main-page';

describe('MainPageComponent', () => {
  let navigateCalls: unknown[][];

  beforeEach(async () => {
    navigateCalls = [];

    await TestBed.configureTestingModule({
      imports: [MainPageComponent],
      providers: [
        {
          provide: Router,
          useValue: {
            navigate: (commands: unknown[]) => {
              navigateCalls.push(commands);
              return Promise.resolve(true);
            }
          }
        }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(MainPageComponent);
    const component = fixture.componentInstance;

    expect(component).toBeTruthy();
  });

  it('should navigate to /movies when browseMovies is called', () => {
    const fixture = TestBed.createComponent(MainPageComponent);
    const component = fixture.componentInstance;

    component.browseMovies();

    expect(navigateCalls.length).toBe(1);
    expect((navigateCalls[0] as string[])[0]).toBe('/movies');
  });

  it('should expose quick search action for home page (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(MainPageComponent);
    const component = fixture.componentInstance;

    expect(typeof (component as any).searchByTitle).toBe('function');
  });
});
