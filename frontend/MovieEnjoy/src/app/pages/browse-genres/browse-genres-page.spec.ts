import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { BrowseGenresPageComponent } from './browse-genres-page';

describe('BrowseGenres Phase 3 Expectations', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowseGenresPageComponent, HttpClientTestingModule]
    }).compileComponents();
  });

  it('should expose parsed genres model (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(BrowseGenresPageComponent);
    const component = fixture.componentInstance as any;

    expect(Array.isArray(component.genres)).toBe(true);
  });

  it('should expose genre selection action (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(BrowseGenresPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.selectGenre).toBe('function');
  });
});
