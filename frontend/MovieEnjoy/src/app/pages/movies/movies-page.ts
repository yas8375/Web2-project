import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Movie, MovieService } from '../../movie.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment.development';

@Component({
  selector: 'app-movies-page',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './movies-page.html',
})
export class MoviesPageComponent implements OnInit {
  movies: Movie[] = [];
  errorMessage = '';
  loading = false;
  title = '';
  year = '';
  director = '';
  star = '';
  genre = '';
  letter = '';
  page = 1;
  size = 50;
  sort = 'title';
  order = 'asc';
  pageSizes = [10, 25, 50, 100];

  constructor(
    private movieService: MovieService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.title = (this.route.snapshot.queryParamMap.get('title') ?? '').trim();
    this.year = (this.route.snapshot.queryParamMap.get('year') ?? '').trim();
    this.director = (this.route.snapshot.queryParamMap.get('director') ?? '').trim();
    this.star = (this.route.snapshot.queryParamMap.get('star') ?? '').trim();
    this.genre = (this.route.snapshot.queryParamMap.get('genre') ?? '').trim();
    this.letter = (this.route.snapshot.queryParamMap.get('letter') ?? '').trim();
    const pageParam = Number(this.route.snapshot.queryParamMap.get('page') ?? '1');
    const sizeParam = Number(this.route.snapshot.queryParamMap.get('size') ?? '50');
    const sortParam = (this.route.snapshot.queryParamMap.get('sort') ?? 'title').trim();
    const orderParam = (this.route.snapshot.queryParamMap.get('order') ?? 'asc').trim();
    this.page = Number.isFinite(pageParam) && pageParam > 0 ? pageParam : 1;
    this.size = Number.isFinite(sizeParam) && sizeParam > 0 ? sizeParam : 50;
    this.sort = sortParam || 'title';
    this.order = orderParam === 'desc' ? 'desc' : 'asc';
    this.loadMovies();
  }

  loadMovies(): void {
    this.loading = true;
    this.errorMessage = '';
    this.movieService.getMovies({
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
    }).subscribe({
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
    this.page += 1;
    this.loadMovies();
  }

  prevPage(): void {
    if (this.page > 1) {
      this.page -= 1;
      this.loadMovies();
    }
  }

  onPageSizeChange(value: number): void {
    this.size = Number.isFinite(value) && value > 0 ? value : 50;
    this.page = 1;
    this.loadMovies();
  }

  onSortChange(value: string): void {
    this.sort = value || 'title';
    this.page = 1;
    this.loadMovies();
  }

  onOrderChange(value: string): void {
    this.order = value === 'desc' ? 'desc' : 'asc';
    this.page = 1;
    this.loadMovies();
  }

 addToCart(movieId: string): void {
  this.http.post(
    `${environment.apiBaseUrl}/api/cart/items`,
    { movieId, quantity: 1 },
    { withCredentials: true }
  ).subscribe({
    next: () => {
      this.router.navigate(['/cart']);
    },
    error: (err) => {
      this.errorMessage = err?.error?.message ?? 'Failed to add movie to cart';
      this.cdr.detectChanges();
    }
  });
}
}
