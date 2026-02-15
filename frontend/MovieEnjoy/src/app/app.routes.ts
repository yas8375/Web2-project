import { Routes } from '@angular/router';
import { MainPageComponent } from './pages/main/main-page';
import { LoginPageComponent } from './pages/login/login-page';
import { MoviesPageComponent } from './pages/movies/movies-page';
import { MovieDetailsPageComponent } from './pages/movie-details/movie-details-page';
import { StarDetailsPageComponent } from './pages/star-details/star-details-page';
import { BrowseGenresPageComponent } from './pages/browse-genres/browse-genres-page';
import { BrowseTitlesPageComponent } from './pages/browse-titles/browse-titles-page';
import { CartPageComponent } from './pages/cart/cart-page';
import { CheckoutPageComponent } from './pages/checkout/checkout-page';
import { ConfirmationPageComponent } from './pages/confirmation/confirmation-page';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'main' },
  { path: 'main', component: MainPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'movies', component: MoviesPageComponent },
  { path: 'movies/:movieId', component: MovieDetailsPageComponent },
  { path: 'stars/:starId', component: StarDetailsPageComponent },
  { path: 'browse/genres', component: BrowseGenresPageComponent },
  { path: 'browse/titles', component: BrowseTitlesPageComponent },
  { path: 'cart', component: CartPageComponent },
  { path: 'checkout', component: CheckoutPageComponent },
  { path: 'confirmation', component: ConfirmationPageComponent },
  { path: '**', redirectTo: 'main' }
];
