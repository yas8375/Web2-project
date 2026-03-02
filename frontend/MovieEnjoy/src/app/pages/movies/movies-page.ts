import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
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

  constructor(
    private movieService: MovieService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadMovies();
  }

  loadMovies(): void {
    this.loading = true;
    this.errorMessage = '';
    this.movieService.getMovies().subscribe({
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
