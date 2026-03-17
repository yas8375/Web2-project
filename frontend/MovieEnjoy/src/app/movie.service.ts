import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface Movie {
  id: string;
  title: string;
  year: number;
  director: string;
  rating: number | null;
  genres: GenreLink[];
  stars: StarLink[];
}

export interface GenreLink {
  id: number;
  name: string;
}

export interface StarLink {
  id: string;
  name: string;
}

export interface MovieSearchParams {
  title?: string;
  year?: string;
  director?: string;
  star?: string;
  genre?: string;
  letter?: string;
  page?: number;
  size?: number;
  sort?: string;
  order?: string;
}

@Injectable({ providedIn: 'root' })
export class MovieService {
  private api = `${environment.apiBaseUrl}/api/movies`;

  constructor(private http: HttpClient) {}

  getMovies(params?: MovieSearchParams): Observable<Movie[]> {
    let httpParams = new HttpParams();
    const safeParams = params ?? {};

    if (safeParams.title) {
      httpParams = httpParams.set('title', safeParams.title);
    }
    if (safeParams.year) {
      httpParams = httpParams.set('year', safeParams.year);
    }
    if (safeParams.director) {
      httpParams = httpParams.set('director', safeParams.director);
    }
    if (safeParams.star) {
      httpParams = httpParams.set('star', safeParams.star);
    }
    if (safeParams.genre) {
      httpParams = httpParams.set('genre', safeParams.genre);
    }
    if (safeParams.letter) {
      httpParams = httpParams.set('letter', safeParams.letter);
    }

    httpParams = httpParams.set('page', String(safeParams.page ?? 1));
    httpParams = httpParams.set('size', String(safeParams.size ?? 50));

    if (safeParams.sort) {
      httpParams = httpParams.set('sort', safeParams.sort);
    }
    if (safeParams.order) {
      httpParams = httpParams.set('order', safeParams.order);
    }

    return this.http.get<Movie[]>(this.api, { params: httpParams });
  }

  getMovieById(movieId: string): Observable<Movie> {
    return this.http.get<Movie>(`${this.api}/${movieId}`);
  }
}
