import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { StarDetailsPageComponent } from './star-details-page';
import { environment } from '../../../environments/environment';

describe('StarDetailsPageComponent', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StarDetailsPageComponent, HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap({ starId: 'nm123' }) } }
        }
      ]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should request star details by id', () => {
    const fixture = TestBed.createComponent(StarDetailsPageComponent);
    const component = fixture.componentInstance;

    fixture.detectChanges();
    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/stars/nm123`);
    expect(req.request.method).toBe('GET');
    req.flush({ id: 'nm123', name: 'Star Name' });

    expect(component.response?.id).toBe('nm123');
  });

  it('should set fallback message on backend error', () => {
    const fixture = TestBed.createComponent(StarDetailsPageComponent);
    const component = fixture.componentInstance;

    fixture.detectChanges();
    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/stars/nm123`);
    req.flush({}, { status: 501, statusText: 'Not Implemented' });

    expect(component.errorMessage).toBe('Not implemented yet');
  });

  it('should expose movies list model for star filmography (Phase 4 expectation)', () => {
    const fixture = TestBed.createComponent(StarDetailsPageComponent);
    const component = fixture.componentInstance;

    expect(Array.isArray((component as any).movies)).toBe(true);

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/api/stars/nm123`);
    req.flush({});
  });
});
