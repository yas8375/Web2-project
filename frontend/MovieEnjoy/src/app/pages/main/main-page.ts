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
}
