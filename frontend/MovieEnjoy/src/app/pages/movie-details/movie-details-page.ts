import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { environment } from '../../../environments/environment.development';
import { MovieService, SingleMovie, StarSummary } from '../../movie.service';

@Component({
  selector: 'app-movie-details-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './movie-details-page.html',
  styleUrl: './movie-details-page.css'
})
export class MovieDetailsPageComponent implements OnInit {
  movie?: SingleMovie;
  stars: StarSummary[] = [];
  errorMessage = '';
  loading = false;
  quantity = 1;

  constructor(
    private route: ActivatedRoute,
    private movieService: MovieService,
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('movieId');
    if (!id) {
      this.errorMessage = 'Movie id is required';
      return;
    }

    this.loading = true;
    this.movieService.getMovieById(id).subscribe({
      next: (data) => {
        this.movie = data;
        this.stars = data.stars ?? [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Movie not found';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  decreaseQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  increaseQuantity(): void {
    this.quantity++;
  }

  addToCart(movieId: string): void {
    this.http.post(
      `${environment.apiBaseUrl}/api/cart/items`,
      { movieId, quantity: this.quantity },
      { withCredentials: true }
    ).subscribe({
      next: () => this.router.navigate(['/cart']),
      error: (err) => {
        this.errorMessage = err?.error?.message ?? 'Failed to add movie to cart';
        this.cdr.detectChanges();
      }
    });
  }
}
