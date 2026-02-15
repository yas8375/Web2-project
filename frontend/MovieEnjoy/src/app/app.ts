import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MovieService, Movie } from './movie.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  movies: Movie[] = [];
  error = '';

  constructor(private movieService: MovieService) {}

  ngOnInit(): void {
    this.movieService.getMovies().subscribe({
      next: (data) => (this.movies = data),
      error: () => (this.error = 'Failed to load movies')
    });
  }
}
