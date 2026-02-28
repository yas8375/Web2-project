import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { MovieDetailsPageComponent } from './movie-details-page';
import { MovieService } from '../../movie.service';

describe('MovieDetails Phase 3 Expectations', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovieDetailsPageComponent],
      providers: [
        { provide: MovieService, useValue: { getMovieById: () => of({ id: 'tt1', title: 'x', year: 2000, director: 'y' }) } },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: convertToParamMap({ movieId: 'tt1' }) } } }
      ]
    }).compileComponents();
  });

  it('should expose cast list model (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(MovieDetailsPageComponent);
    const component = fixture.componentInstance as any;

    expect(Array.isArray(component.stars)).toBe(true);
  });

  it('should expose add-to-cart action from details page (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(MovieDetailsPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.addToCart).toBe('function');
  });
});
