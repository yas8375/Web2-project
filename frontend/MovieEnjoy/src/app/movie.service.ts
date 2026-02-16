import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Movie {
  id: string;
  title: string;
  year: number;
  director: string;
}

@Injectable({ providedIn: 'root' })
export class MovieService {
  private api = 'http://localhost:8081/api/movies';

  constructor(private http: HttpClient) {}

  getMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.api}?page=1&size=50`);
  }

  getMovieById(movieId: string): Observable<Movie> {
    return this.http.get<Movie>(`${this.api}/${movieId}`);
  }
}
