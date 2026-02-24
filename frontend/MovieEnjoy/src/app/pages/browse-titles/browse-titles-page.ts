import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-browse-titles-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './browse-titles-page.html'
})
export class BrowseTitlesPageComponent implements OnInit {
  response: any;
  errorMessage = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get(`${environment.apiBaseUrl}/api/titles`).subscribe({
      next: (data) => this.response = data,
      error: (err) => this.errorMessage = err?.error?.message ?? 'Not implemented yet'
    });
  }
}
