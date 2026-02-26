import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { MovieDetailsPageComponent } from './movie-details-page';
import { Movie, MovieService } from '../../movie.service';

describe('MovieDetailsPageComponent', () => {
  const mockMovie: Movie = {
    id: 'tt123',
    title: 'Sample Movie',
    year: 2020,
    director: 'Sample Director'
  };

  it('should load movie details by route id', async () => {
    const movieServiceStub: Pick<MovieService, 'getMovieById'> = {
      getMovieById: () => of(mockMovie)
    };

    await TestBed.configureTestingModule({
      imports: [MovieDetailsPageComponent],
      providers: [
        { provide: MovieService, useValue: movieServiceStub },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap({ movieId: 'tt123' }) } }
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(MovieDetailsPageComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.movie?.id).toBe('tt123');
    expect(component.errorMessage).toBe('');
  });

  it('should show not found error when movie request fails', async () => {
    const movieServiceStub: Pick<MovieService, 'getMovieById'> = {
      getMovieById: () => throwError(() => new Error('not found'))
    };

    await TestBed.configureTestingModule({
      imports: [MovieDetailsPageComponent],
      providers: [
        { provide: MovieService, useValue: movieServiceStub },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap({ movieId: 'tt404' }) } }
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(MovieDetailsPageComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.errorMessage).toBe('Movie not found');
  });

  it('should expose cast list model for clickable stars (Phase 4 expectation)', async () => {
    const movieServiceStub: Pick<MovieService, 'getMovieById'> = {
      getMovieById: () => of(mockMovie)
    };

    await TestBed.configureTestingModule({
      imports: [MovieDetailsPageComponent],
      providers: [
        { provide: MovieService, useValue: movieServiceStub },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap({ movieId: 'tt123' }) } }
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(MovieDetailsPageComponent);
    const component = fixture.componentInstance;

    expect(Array.isArray((component as any).stars)).toBe(true);
  });
});
