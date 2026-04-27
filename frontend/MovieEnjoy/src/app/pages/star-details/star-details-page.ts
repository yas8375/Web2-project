import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { MovieService, MovieSummary, SingleStar } from '../../movie.service';

@Component({
  selector: 'app-star-details-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './star-details-page.html',
  styleUrl: './star-details-page.css',
})
export class StarDetailsPageComponent implements OnInit {
  star?: SingleStar;
  movies: MovieSummary[] = [];
  errorMessage = '';
  loading = false;
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(
    private route: ActivatedRoute,
    private movieService: MovieService,
    private cdr: ChangeDetectorRef,
    private location: Location,
  ) {}

  ngOnInit(): void {
    const starId = this.route.snapshot.paramMap.get('starId');
    if (!starId) {
      this.errorMessage = 'Star id is required';
      return;
    }

    this.loading = true;
    this.movieService.getStarById(starId).subscribe({
      next: (data) => {
        this.star = data;
        this.movies = [...(data.movies ?? [])];
        this.sortMovies(this.sortDirection);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message ?? 'Star not found';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  sortMovies(direction: 'asc' | 'desc'): void {
    this.sortDirection = direction;
    const multiplier = direction === 'asc' ? 1 : -1;
    this.movies = [...this.movies].sort(
      (left, right) =>
        multiplier *
        (left.title ?? '').localeCompare(right.title ?? '', undefined, {
          sensitivity: 'base',
        }),
    );
  }

  onSortChange(direction: string): void {
    if (direction === 'asc' || direction === 'desc') {
      this.sortMovies(direction);
    }
  }

  goBack(): void {
    this.location.back();
  }
}
