import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { BrowseGenresPageComponent } from './browse-genres-page';
import { environment } from '../../../environments/environment';

describe('BrowseGenresPageComponent', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowseGenresPageComponent, HttpClientTestingModule]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(BrowseGenresPageComponent);
    const component = fixture.componentInstance;

    component.ngOnInit();
    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/genres`);
    req.flush({});

    expect(component).toBeTruthy();
  });

  it('should request genres endpoint on init and store response', () => {
    const fixture = TestBed.createComponent(BrowseGenresPageComponent);
    const component = fixture.componentInstance;
    const payload = { page: 'browse-genres', endpoint: 'GET /api/genres' };

    component.ngOnInit();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/genres`);
    expect(req.request.method).toBe('GET');
    req.flush(payload);

    expect(component.response).toEqual(payload);
  });

  it('should set backend error message when request fails', () => {
    const fixture = TestBed.createComponent(BrowseGenresPageComponent);
    const component = fixture.componentInstance;

    component.ngOnInit();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/genres`);
    req.flush({ message: 'Planned for next phase' }, { status: 501, statusText: 'Not Implemented' });

    expect(component.errorMessage).toBe('Planned for next phase');
  });

  it('should use fallback message when error has no message', () => {
    const fixture = TestBed.createComponent(BrowseGenresPageComponent);
    const component = fixture.componentInstance;

    component.ngOnInit();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/genres`);
    req.flush({}, { status: 500, statusText: 'Server Error' });

    expect(component.errorMessage).toBe('Not implemented yet');
  });
});
