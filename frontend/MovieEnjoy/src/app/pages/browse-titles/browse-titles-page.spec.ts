import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { BrowseTitlesPageComponent } from './browse-titles-page';
import { environment } from '../../../environments/environment';

describe('BrowseTitlesPageComponent', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowseTitlesPageComponent, HttpClientTestingModule]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(BrowseTitlesPageComponent);
    const component = fixture.componentInstance;

    component.ngOnInit();
    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/titles`);
    req.flush({});

    expect(component).toBeTruthy();
  });

  it('should request titles endpoint on init and store response', () => {
    const fixture = TestBed.createComponent(BrowseTitlesPageComponent);
    const component = fixture.componentInstance;
    const payload = { page: 'browse-titles', endpoint: 'GET /api/titles' };

    component.ngOnInit();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/titles`);
    expect(req.request.method).toBe('GET');
    req.flush(payload);

    expect(component.response).toEqual(payload);
  });

  it('should set backend error message when request fails', () => {
    const fixture = TestBed.createComponent(BrowseTitlesPageComponent);
    const component = fixture.componentInstance;

    component.ngOnInit();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/titles`);
    req.flush({ message: 'Planned for next phase' }, { status: 501, statusText: 'Not Implemented' });

    expect(component.errorMessage).toBe('Planned for next phase');
  });

  it('should use fallback message when error has no message', () => {
    const fixture = TestBed.createComponent(BrowseTitlesPageComponent);
    const component = fixture.componentInstance;

    component.ngOnInit();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/titles`);
    req.flush({}, { status: 500, statusText: 'Server Error' });

    expect(component.errorMessage).toBe('Not implemented yet');
  });

  it('should expose title letters model for A-Z browsing (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(BrowseTitlesPageComponent);
    const component = fixture.componentInstance;

    expect(Array.isArray((component as any).letters)).toBe(true);
    expect((component as any).letters?.length).toBeGreaterThanOrEqual(27);
  });
});
