import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './main-page.html',
  styleUrl: './main-page.css',
})
export class MainPageComponent implements OnInit {
  private readonly browseTokens = [
    '0',
    '1',
    '2',
    '3',
    '4',
    '5',
    '6',
    '7',
    '8',
    '9',
    'A',
    'B',
    'C',
    'D',
    'E',
    'F',
    'G',
    'H',
    'I',
    'J',
    'K',
    'L',
    'M',
    'N',
    'O',
    'P',
    'Q',
    'R',
    'S',
    'T',
    'U',
    'V',
    'W',
    'X',
    'Y',
    'Z',
  ];

  genres: Array<{ id: number; name: string }> = [];
  genresLoading = false;
  errorMessage = '';
  title = '';
  year = '';
  director = '';
  star = '';

  constructor(
    private router: Router,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadGenres();
  }

  browseMovies(): void {
    this.router.navigate(['/movies']);
  }

  searchMovies(): void {
    const queryParams: Record<string, string> = {};

    const safeTitle = this.title.trim();
    const safeYear = this.year.trim();
    const safeDirector = this.director.trim();
    const safeStar = this.star.trim();

    if (safeTitle) {
      queryParams['title'] = safeTitle;
    }
    if (safeYear) {
      queryParams['year'] = safeYear;
    }
    if (safeDirector) {
      queryParams['director'] = safeDirector;
    }
    if (safeStar) {
      queryParams['star'] = safeStar;
    }

    this.router.navigate(['/movies'], { queryParams });
  }

  searchByTitle(): void {
    this.searchMovies();
  }

  browseByGenre(genre?: string): void {
    const queryParams = genre ? { genre } : undefined;
    this.router.navigate(['/movies'], { queryParams });
  }

  clearSearch(): void {
    this.title = '';
    this.year = '';
    this.director = '';
    this.star = '';
  }

  get letters(): string[] {
    return this.browseTokens;
  }

  private loadGenres(): void {
    this.genresLoading = true;
    this.http.get(`${environment.apiBaseUrl}/api/genres`).subscribe({
      next: (data: any) => {
        this.genres = this.normalizeGenres(data);
        this.genresLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message ?? 'Failed to load genres';
        this.genresLoading = false;
        this.cdr.detectChanges();
      },
    });
  }

  private normalizeGenres(data: unknown): Array<{ id: number; name: string }> {
    if (!Array.isArray(data)) return [];
    return data
      .map((item: any, index) => {
        if (typeof item === 'string') return { id: index + 1, name: item };
        if (item && typeof item.name === 'string') {
          return { id: Number(item.id ?? index + 1), name: item.name };
        }
        return null;
      })
      .filter((item): item is { id: number; name: string } => item !== null);
  }
}
