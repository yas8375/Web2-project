import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [],
  templateUrl: './main-page.html'
})
export class MainPageComponent {
  constructor(private router: Router) {}

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
}
