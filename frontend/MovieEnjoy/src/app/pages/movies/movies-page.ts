import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Movie, MovieService } from '../../movie.service';

@Component({
  selector: 'app-movies-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './movies-page.html'
})
export class MoviesPageComponent implements OnInit {
  movies: Movie[] = [];
  errorMessage = '';

  constructor(private movieService: MovieService) {}

  ngOnInit(): void {
    this.movieService.getMovies().subscribe({
      next: (data) => this.movies = data,
      error: () => this.errorMessage = 'Failed to load movies'
    });
  }

  addToCart(movieId: string): void {
    console.log('Add to cart', movieId);
  }
}
