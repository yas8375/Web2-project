import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-browse-titles-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './browse-titles-page.html',
})
export class BrowseTitlesPageComponent implements OnInit {
  letters: string[] = [
    '0',
    '1',
    '2',
    '3',
    '4',
    '5',
    '6',
    '7',
    '8',
    '9',
    'A',
    'B',
    'C',
    'D',
    'E',
    'F',
    'G',
    'H',
    'I',
    'J',
    'K',
    'L',
    'M',
    'N',
    'O',
    'P',
    'Q',
    'R',
    'S',
    'T',
    'U',
    'V',
    'W',
    'X',
    'Y',
    'Z',
  ];
  errorMessage = '';

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.http.get(`${environment.apiBaseUrl}/api/titles`).subscribe({
      next: (data: any) =>
        (this.letters =
          Array.isArray(data) && data.length > 0 ? data : this.letters),
      error: (err) =>
        (this.errorMessage = err?.error?.message ?? 'Not implemented yet'),
    });
  }

  selectLetter(letter: string): void {
    this.router.navigate(['/movies'], { queryParams: { letter } });
  }
}
