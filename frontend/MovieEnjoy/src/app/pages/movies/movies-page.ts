import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Movie, MovieService } from '../../movie.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
import { AuthStateService } from '../../auth-state.service';
import { CartStateService } from '../../cart-state.service';

@Component({
  selector: 'app-movies-page',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './movies-page.html',
  styleUrl: './movies-page.css',
})
export class MoviesPageComponent implements OnInit {
  private readonly destroyRef = inject(DestroyRef);
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

  movies: Movie[] = [];
  errorMessage = '';
  successMessage = '';
  loading = false;
  title = '';
  year = '';
  director = '';
  star = '';
  genre = '';
  letter = '';
  page = 1;
  size = 20;
  sort = 'title';
  order = 'asc';
  pageSizes = [10, 20, 50, 100];
  genres: Array<{ id: number; name: string }> = [];

  constructor(
    private movieService: MovieService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private http: HttpClient,
    private router: Router,
    private authState: AuthStateService,
    private cartState: CartStateService,
  ) {}

  ngOnInit(): void {
    this.loadGenres();

    this.route.queryParamMap
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((params) => {
        this.title = (params.get('title') ?? '').trim();
        this.year = (params.get('year') ?? '').trim();
        this.director = (params.get('director') ?? '').trim();
        this.star = (params.get('star') ?? '').trim();
        this.genre = (params.get('genre') ?? '').trim();
        this.letter = (params.get('letter') ?? '').trim();

        const pageParam = Number(params.get('page') ?? '1');
        const sizeParam = Number(params.get('size') ?? '20');
        const sortParam = (params.get('sort') ?? 'title').trim();
        const orderParam = (params.get('order') ?? 'asc').trim();

        this.page = Number.isFinite(pageParam) && pageParam > 0 ? pageParam : 1;
        this.size =
          Number.isFinite(sizeParam) && sizeParam > 0 ? sizeParam : 20;
        this.sort = sortParam || 'title';
        this.order = orderParam === 'desc' ? 'desc' : 'asc';

        this.loadMovies();
      });
  }

  loadMovies(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.movieService
      .getMovies({
        title: this.title || undefined,
        year: this.year || undefined,
        director: this.director || undefined,
        star: this.star || undefined,
        genre: this.genre || undefined,
        letter: this.letter || undefined,
        page: this.page,
        size: this.size,
        sort: this.sort,
        order: this.order,
      })
      .subscribe({
        next: (data) => {
          this.movies = Array.isArray(data) ? data : [];
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.errorMessage = 'Failed to load movies';
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }

  nextPage(): void {
    this.updateQueryParams({ page: this.page + 1 });
  }

  prevPage(): void {
    if (this.page > 1) {
      this.updateQueryParams({ page: this.page - 1 });
    }
  }

  previousPage(): void {
    this.prevPage();
  }

  onPageSizeChange(value: number): void {
    const size = Number.isFinite(value) && value > 0 ? value : 20;
    this.updateQueryParams({ size, page: 1 });
  }

  onSortChange(value: string): void {
    this.updateQueryParams({ sort: value || 'title', page: 1 });
  }

  onOrderChange(value: string): void {
    this.updateQueryParams({
      order: value === 'desc' ? 'desc' : 'asc',
      page: 1,
    });
  }

  searchMovies(): void {
    this.updateQueryParams({
      title: this.title,
      year: this.year,
      director: this.director,
      star: this.star,
      genre: this.genre,
      letter: this.letter,
      page: 1,
    });
  }

  clearFilters(): void {
    this.title = '';
    this.year = '';
    this.director = '';
    this.star = '';
    this.genre = '';
    this.letter = '';
    this.updateQueryParams({
      title: null,
      year: null,
      director: null,
      star: null,
      genre: null,
      letter: null,
      page: 1,
      sort: this.sort,
      order: this.order,
      size: this.size,
    });
  }

  selectGenre(genre: string): void {
    this.genre = this.genre === genre ? '' : genre;
    this.updateQueryParams({ genre: this.genre || null, page: 1 });
  }

  selectLetter(letter: string): void {
    this.letter = this.letter === letter ? '' : letter;
    this.updateQueryParams({ letter: this.letter || null, page: 1 });
  }

  addToCart(movieId: string): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.authState.isLoggedIn()) {
      this.router.navigate(['/login'], {
        queryParams: {
          returnUrl: this.router.url,
          message: 'Please sign in before adding movies to the cart.',
        },
      });
      return;
    }

    this.http
      .post(
        `${environment.apiBaseUrl}/api/cart/items`,
        { movieId, quantity: 1 },
        { withCredentials: true },
      )
      .subscribe({
        next: () => {
          this.cartState.notifyChanged();
          this.successMessage = 'Movie added to cart successfully.';
          this.cdr.detectChanges();

          setTimeout(() => {
            this.successMessage = '';
            this.cdr.detectChanges();
          }, 2500);
        },
        error: (err) => {
          if (err?.status === 401) {
            this.router.navigate(['/login'], {
              queryParams: {
                returnUrl: this.router.url,
                message: 'Please sign in before adding movies to the cart.',
              },
            });
            return;
          }

          this.errorMessage =
            err?.error?.message ?? 'Failed to add movie to cart';
          this.cdr.detectChanges();
        },
      });
  }

  get currentPage(): number {
    return this.page;
  }

  get totalPages(): number {
    return this.movies.length < this.size ? this.page : this.page + 1;
  }

  get hasNextPage(): boolean {
    return this.movies.length === this.size;
  }

  get hasActiveFilters(): boolean {
    return Boolean(
      this.title ||
      this.year ||
      this.director ||
      this.star ||
      this.genre ||
      this.letter,
    );
  }

  get displayedGenres(): string[] {
    return this.genres.slice(0, 8).map((item) => item.name);
  }

  get displayedLetters(): string[] {
    return this.browseTokens;
  }

  get activeBrowseLabel(): string {
    if (this.genre) {
      return this.genre;
    }

    if (this.letter) {
      return this.letter;
    }

    return 'All Movies';
  }

  movieInitials(title: string): string {
    const words = title
      .split(/\s+/)
      .map((word) => word.trim())
      .filter(Boolean);

    return (
      words
        .slice(0, 2)
        .map((word) => word[0]?.toUpperCase() ?? '')
        .join('') || '?'
    );
  }

  private loadGenres(): void {
    this.http
      .get<
        Array<{ id: number; name: string }>
      >(`${environment.apiBaseUrl}/api/genres`)
      .subscribe({
        next: (data) => {
          this.genres = Array.isArray(data) ? data : [];
          this.cdr.detectChanges();
        },
        error: () => {
          this.genres = [];
          this.cdr.detectChanges();
        },
      });
  }

  private updateQueryParams(
    updates: Record<string, string | number | null>,
  ): void {
    const queryParams: Record<string, string | number> = {
      title: this.title,
      year: this.year,
      director: this.director,
      star: this.star,
      genre: this.genre,
      letter: this.letter,
      page: this.page,
      size: this.size,
      sort: this.sort,
      order: this.order,
    };

    for (const [key, value] of Object.entries(updates)) {
      if (value === null || value === '') {
        delete queryParams[key];
      } else {
        queryParams[key] = value;
      }
    }

    this.router.navigate(['/movies'], { queryParams });
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
