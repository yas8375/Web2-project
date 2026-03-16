import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { MovieDetailsPageComponent } from './movie-details-page';
import { MovieService } from '../../movie.service';

describe('MovieDetailsPageComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovieDetailsPageComponent, HttpClientTestingModule],
      providers: [
        provideRouter([]),
        {
          provide: MovieService,
          useValue: {
            getMovieById: () =>
              of({
                id: 'tt1',
                title: 'Movie One',
                year: 2000,
                director: 'Director One',
                rating: 8.1,
                genres: ['Drama'],
                stars: [{ id: 'nm1', name: 'Star One' }],
              }),
          },
        },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap({ movieId: 'tt1' }) } },
        },
      ],
    }).compileComponents();
  });

  it('should expose cast list model', () => {
    const fixture = TestBed.createComponent(MovieDetailsPageComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    expect(Array.isArray(component.stars)).toBe(true);
    expect(component.stars.length).toBe(1);
  });

  it('should expose add-to-cart action from details page', () => {
    const fixture = TestBed.createComponent(MovieDetailsPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.addToCart).toBe('function');
  });
});
