import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-browse-genres-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './browse-genres-page.html'
})
export class BrowseGenresPageComponent implements OnInit {
  genres: Array<{ id: number; name: string }> = [];
  errorMessage = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get(`${environment.apiBaseUrl}/api/genres`).subscribe({
      next: (data: any) => this.genres = Array.isArray(data) ? data : [],
      error: (err) => this.errorMessage = err?.error?.message ?? 'Not implemented yet'
    });
  }
}
