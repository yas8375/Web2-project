import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Movie, MovieService } from '../../movie.service';

@Component({
  selector: 'app-movie-details-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './movie-details-page.html'
})
export class MovieDetailsPageComponent implements OnInit {
  movie?: Movie;
  errorMessage = '';

  constructor(private route: ActivatedRoute, private movieService: MovieService) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('movieId');
    if (!id) {
      this.errorMessage = 'Movie id is required';
      return;
    }

    this.movieService.getMovieById(id).subscribe({
      next: (data) => this.movie = data,
      error: () => this.errorMessage = 'Movie not found'
    });
  }
}
