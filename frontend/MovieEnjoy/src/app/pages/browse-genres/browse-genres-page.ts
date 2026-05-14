import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-browse-genres-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './browse-genres-page.html',
  styleUrl: './browse-genres-page.css',
})
export class BrowseGenresPageComponent implements OnInit {
  genres: Array<{ id: number; name: string }> = [];
  errorMessage = '';

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.http.get(`${environment.apiBaseUrl}/api/genres`).subscribe({
      next: (data: any) => (this.genres = this.normalizeGenres(data)),
      error: (err) =>
        (this.errorMessage = err?.error?.message ?? 'Not implemented yet'),
    });
  }

  selectGenre(genre: string): void {
    this.router.navigate(['/movies'], { queryParams: { genre } });
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
}
