import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of, Subject, throwError } from 'rxjs';

import { MoviesPageComponent } from './movies-page';
import { Movie, MovieService } from '../../movie.service';

describe('MoviesPageComponent', () => {
  let fixture: ComponentFixture<MoviesPageComponent>;
  let component: MoviesPageComponent;

  let getMoviesCalls = 0;
  let getMoviesImpl: () => Observable<Movie[]>;

  const movieServiceStub: Pick<MovieService, 'getMovies'> = {
    getMovies: () => {
      getMoviesCalls += 1;
      return getMoviesImpl();
    }
  };

  const mockMovies: Movie[] = [
    { id: 'tt1', title: 'Movie One', year: 2020, director: 'Dir A' },
    { id: 'tt2', title: 'Movie Two', year: 2021, director: 'Dir B' }
  ];

  beforeEach(async () => {
    getMoviesCalls = 0;
    getMoviesImpl = () => of([]);

    await TestBed.configureTestingModule({
      imports: [MoviesPageComponent],
      providers: [{ provide: MovieService, useValue: movieServiceStub }]
    }).compileComponents();

    fixture = TestBed.createComponent(MoviesPageComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should call getMovies on init and render movie rows', () => {
    getMoviesImpl = () => of(mockMovies);
    fixture.detectChanges();

    expect(getMoviesCalls).toBe(1);
    expect(component.movies.length).toBe(2);
    expect(component.loading).toBe(false);

    const rows = fixture.nativeElement.querySelectorAll('tbody tr');
    expect(rows.length).toBe(2);
  });

  it('should show loading state while request is in progress', () => {
    const subject = new Subject<Movie[]>();
    getMoviesImpl = () => subject.asObservable();

    component.loadMovies();
    expect(component.loading).toBe(true);

    subject.next(mockMovies);
    subject.complete();

    expect(component.loading).toBe(false);
    expect(component.movies.length).toBe(2);
  });

  it('should handle service error and show error message', () => {
    getMoviesImpl = () => throwError(() => new Error('network error'));
    fixture.detectChanges();

    expect(component.loading).toBe(false);
    expect(component.errorMessage).toBe('Failed to load movies');
  });

  it('should show "No movies found." when service returns empty list', () => {
    getMoviesImpl = () => of([]);
    fixture.detectChanges();

    expect(component.movies).toEqual([]);
  });

  it('should expose pagination state and actions (Phase 4 expectation)', () => {
    expect(typeof (component as any).currentPage).toBe('number');
    expect(typeof (component as any).nextPage).toBe('function');
    expect(typeof (component as any).previousPage).toBe('function');
  });
});
