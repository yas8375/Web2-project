import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Movie, MovieService } from '../../movie.service';

@Component({
  selector: 'app-movies-page',
  standalone: true,
  imports: [CommonModule],
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

  constructor(
    private movieService: MovieService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.title = (this.route.snapshot.queryParamMap.get('title') ?? '').trim();
    this.year = (this.route.snapshot.queryParamMap.get('year') ?? '').trim();
    this.director = (this.route.snapshot.queryParamMap.get('director') ?? '').trim();
    this.star = (this.route.snapshot.queryParamMap.get('star') ?? '').trim();
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
      page: 1,
      size: 50,
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

  addToCart(movieId: string): void {
    console.log('Add to cart', movieId);
  }
}
