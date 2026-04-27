import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import {
  ActivatedRoute,
  convertToParamMap,
  provideRouter,
} from '@angular/router';
import { of } from 'rxjs';

import { MovieService } from '../../movie.service';
import { StarDetailsPageComponent } from './star-details-page';

describe('StarDetailsPageComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StarDetailsPageComponent, HttpClientTestingModule],
      providers: [
        provideRouter([]),
        {
          provide: MovieService,
          useValue: {
            getStarById: () =>
              of({
                id: 'nm1',
                name: 'Star One',
                birthYear: 1970,
                movies: [
                  {
                    id: 'tt1',
                    title: 'Movie One',
                    year: 2000,
                    director: 'Director One',
                    rating: 8.1,
                  },
                ],
              }),
          },
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: convertToParamMap({ starId: 'nm1' }) },
          },
        },
      ],
    }).compileComponents();
  });

  it('should expose filmography model', () => {
    const fixture = TestBed.createComponent(StarDetailsPageComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    expect(Array.isArray(component.movies)).toBe(true);
    expect(component.movies.length).toBe(1);
  });

  it('should expose sorting action for star movies', () => {
    const fixture = TestBed.createComponent(StarDetailsPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.sortMovies).toBe('function');
  });

  it('should sort movies in descending order when requested', () => {
    const fixture = TestBed.createComponent(StarDetailsPageComponent);
    const component = fixture.componentInstance;
    component.movies = [
      { id: 'tt2', title: 'Bravo', year: 2002, director: 'D2', rating: 7.2 },
      { id: 'tt1', title: 'Alpha', year: 2001, director: 'D1', rating: 7.1 },
    ];

    component.sortMovies('desc');

    expect(component.movies.map((movie) => movie.title)).toEqual([
      'Bravo',
      'Alpha',
    ]);
  });
});
