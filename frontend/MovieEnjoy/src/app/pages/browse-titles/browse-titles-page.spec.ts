import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { BrowseTitlesPageComponent } from './browse-titles-page';

describe('BrowseTitles Phase 3 Expectations', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowseTitlesPageComponent, HttpClientTestingModule]
    }).compileComponents();
  });

  it('should expose title letters model for A-Z browse (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(BrowseTitlesPageComponent);
    const component = fixture.componentInstance as any;

    expect(Array.isArray(component.letters)).toBe(true);
    expect(component.letters?.length).toBeGreaterThanOrEqual(27);
  });

  it('should expose letter selection action (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(BrowseTitlesPageComponent);
    const component = fixture.componentInstance as any;

    expect(typeof component.selectLetter).toBe('function');
  });
});
