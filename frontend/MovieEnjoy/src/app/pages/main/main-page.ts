import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './main-page.html'
})
export class MainPageComponent implements OnInit {
  genres: Array<{ id: number; name: string }> = [];
  letters: string[] = [];
  genresLoading = false;
  lettersLoading = false;
  errorMessage = '';

  constructor(
    private router: Router,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadGenres();
    this.loadTitles();
  }

  browseMovies(): void {
    this.router.navigate(['/movies']);
  }

  searchMovies(title: string, year: string, director: string, star: string): void {
    const queryParams: Record<string, string> = {};

    const safeTitle = title.trim();
    const safeYear = year.trim();
    const safeDirector = director.trim();
    const safeStar = star.trim();

    if (safeTitle) {
      queryParams['title'] = safeTitle;
    }
    if (safeYear) {
      queryParams['year'] = safeYear;
    }
    if (safeDirector) {
      queryParams['director'] = safeDirector;
    }
    if (safeStar) {
      queryParams['star'] = safeStar;
    }

    this.router.navigate(['/movies'], { queryParams });
  }

  private loadGenres(): void {
    this.genresLoading = true;
    this.http.get(`${environment.apiBaseUrl}/api/genres`).subscribe({
      next: (data: any) => {
        this.genres = Array.isArray(data) ? data : [];
        this.genresLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message ?? 'Failed to load genres';
        this.genresLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private loadTitles(): void {
    this.lettersLoading = true;
    this.http.get(`${environment.apiBaseUrl}/api/titles`).subscribe({
      next: (data: any) => {
        this.letters = Array.isArray(data) ? data : [];
        this.lettersLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message ?? 'Failed to load titles';
        this.lettersLoading = false;
        this.cdr.detectChanges();
      }
    });
  }
}
