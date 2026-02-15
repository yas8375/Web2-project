import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-star-details-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './star-details-page.html'
})
export class StarDetailsPageComponent implements OnInit {
  response: any;
  errorMessage = '';

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    const starId = this.route.snapshot.paramMap.get('starId');
    if (!starId) {
      this.errorMessage = 'Star id is required';
      return;
    }

    this.http.get(`http://localhost:8081/api/stars/${starId}`).subscribe({
      next: (data) => this.response = data,
      error: (err) => this.errorMessage = err?.error?.message ?? 'Not implemented yet'
    });
  }
}
