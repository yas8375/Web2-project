import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { StarDetailsPageComponent } from './star-details-page';

describe('StarDetails Phase 3 Expectations', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StarDetailsPageComponent, HttpClientTestingModule],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: convertToParamMap({ starId: 'nm1' }) } } }
      ]
    }).compileComponents();
  });

  it('should expose filmography model (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(StarDetailsPageComponent);
    const component = fixture.componentInstance as any;

    expect(Array.isArray(component.movies)).toBe(true);
  });

  it('should expose sorting action for star movies (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(StarDetailsPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.sortMovies).toBe('function');
  });
});
