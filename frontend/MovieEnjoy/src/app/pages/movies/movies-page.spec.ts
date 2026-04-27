import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Observable, of } from 'rxjs';

import { Movie, MovieService } from '../../movie.service';
import { MoviesPageComponent } from './movies-page';

describe('MoviesPage Phase 3 Expectations', () => {
  let fixture: ComponentFixture<MoviesPageComponent>;
  let component: MoviesPageComponent;

  let getMoviesImpl: () => Observable<Movie[]>;
  const movieServiceStub: Pick<MovieService, 'getMovies'> = {
    getMovies: () => getMoviesImpl(),
  };

  beforeEach(async () => {
    getMoviesImpl = () => of([]);

    await TestBed.configureTestingModule({
      imports: [MoviesPageComponent, HttpClientTestingModule],
      providers: [
        provideRouter([]),
        { provide: MovieService, useValue: movieServiceStub },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MoviesPageComponent);
    component = fixture.componentInstance;
  });

  it('should expose pagination state (Phase 4 expectation)', () => {
    expect(typeof (component as any).currentPage).toBe('number');
    expect(typeof (component as any).totalPages).toBe('number');
  });

  it('should expose page navigation actions (Phase 4 expectation)', () => {
    expect(typeof (component as any).nextPage).toBe('function');
    expect(typeof (component as any).previousPage).toBe('function');
  });
});
