import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import {
  Subject,
  Subscription,
  debounceTime,
  distinctUntilChanged,
  switchMap,
  of,
} from 'rxjs';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './main-page.html',
  styleUrl: './main-page.css',
})
export class MainPageComponent implements OnInit, OnDestroy {
  private readonly searchStateKey = 'main_search_state';
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
  titleSuggestions: string[] = [];
  showSuggestions = false;
  activeSuggestionIndex = -1;

  private readonly titleInput$ = new Subject<string>();
  private titleInputSub?: Subscription;

  constructor(
    private router: Router,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.restoreSearchState();
    this.loadGenres();
    this.titleInputSub = this.titleInput$
      .pipe(
        debounceTime(180),
        distinctUntilChanged(),
        switchMap((raw) => {
          const query = raw.trim();
          if (query.length < 2) {
            return of([] as string[]);
          }
          return this.http.get<string[]>(
            `${environment.apiBaseUrl}/api/movies/suggest`,
            {
              params: { query, limit: '8' },
            },
          );
        }),
      )
      .subscribe({
        next: (suggestions) => {
          this.titleSuggestions = suggestions ?? [];
          this.activeSuggestionIndex =
            this.titleSuggestions.length > 0 ? 0 : -1;
          this.showSuggestions = this.titleSuggestions.length > 0;
          this.cdr.detectChanges();
        },
        error: () => {
          this.titleSuggestions = [];
          this.activeSuggestionIndex = -1;
          this.showSuggestions = false;
          this.cdr.detectChanges();
        },
      });
  }

  ngOnDestroy(): void {
    this.titleInputSub?.unsubscribe();
  }

  browseMovies(): void {
    this.router.navigate(['/movies']);
  }

  searchMovies(): void {
    this.showSuggestions = false;
    this.titleSuggestions = [];
    this.activeSuggestionIndex = -1;

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

    this.saveSearchState();
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
    this.titleSuggestions = [];
    this.showSuggestions = false;
    this.activeSuggestionIndex = -1;
    this.clearSearchState();
  }

  onTitleInput(value: string): void {
    this.title = value;
    this.titleInput$.next(value);
  }

  onTitleFocus(): void {
    this.showSuggestions = this.titleSuggestions.length > 0;
  }

  onTitleBlur(): void {
    // Delay hide to allow clicking an item in the dropdown.
    setTimeout(() => {
      this.showSuggestions = false;
      this.cdr.detectChanges();
    }, 120);
  }

  onTitleKeydown(event: KeyboardEvent): void {
    if (!this.showSuggestions || this.titleSuggestions.length === 0) {
      return;
    }

    if (event.key === 'ArrowDown') {
      event.preventDefault();
      this.activeSuggestionIndex =
        (this.activeSuggestionIndex + 1) % this.titleSuggestions.length;
      return;
    }

    if (event.key === 'ArrowUp') {
      event.preventDefault();
      this.activeSuggestionIndex =
        this.activeSuggestionIndex <= 0
          ? this.titleSuggestions.length - 1
          : this.activeSuggestionIndex - 1;
      return;
    }

    if (event.key === 'Enter' && this.activeSuggestionIndex >= 0) {
      event.preventDefault();
      this.selectTitleSuggestion(
        this.titleSuggestions[this.activeSuggestionIndex],
      );
    }
  }

  selectTitleSuggestion(suggestion: string): void {
    this.title = suggestion;
    this.showSuggestions = false;
    this.titleSuggestions = [];
    this.activeSuggestionIndex = -1;
    this.searchMovies();
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
        if (typeof item === 'string') {
          return { id: index + 1, name: item };
        }

        if (item && typeof item.name === 'string') {
          return { id: Number(item.id ?? index + 1), name: item.name };
        }

        return null;
      })
      .filter((item): item is { id: number; name: string } => item !== null);
  }

  private saveSearchState(): void {
    if (typeof sessionStorage === 'undefined') {
      return;
    }

    sessionStorage.setItem(
      this.searchStateKey,
      JSON.stringify({
        title: this.title.trim(),
        year: this.year.trim(),
        director: this.director.trim(),
        star: this.star.trim(),
      }),
    );
  }

  private restoreSearchState(): void {
    if (typeof sessionStorage === 'undefined') {
      return;
    }

    const raw = sessionStorage.getItem(this.searchStateKey);
    if (!raw) {
      return;
    }

    try {
      const state = JSON.parse(raw) as Partial<Record<'title' | 'year' | 'director' | 'star', string>>;
      this.title = typeof state.title === 'string' ? state.title : '';
      this.year = typeof state.year === 'string' ? state.year : '';
      this.director = typeof state.director === 'string' ? state.director : '';
      this.star = typeof state.star === 'string' ? state.star : '';
    } catch {
      this.clearSearchState();
    }
  }

  private clearSearchState(): void {
    if (typeof sessionStorage === 'undefined') {
      return;
    }

    sessionStorage.removeItem(this.searchStateKey);
  }
}
