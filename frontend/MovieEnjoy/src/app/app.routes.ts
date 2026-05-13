import { Routes } from '@angular/router';
import { MainPageComponent } from './pages/main/main-page';
import { LoginPageComponent } from './pages/login/login-page';
import { SignupPageComponent } from './pages/signup/signup-page';
import { MoviesPageComponent } from './pages/movies/movies-page';
import { MovieDetailsPageComponent } from './pages/movie-details/movie-details-page';
import { StarDetailsPageComponent } from './pages/star-details/star-details-page';
import { BrowseGenresPageComponent } from './pages/browse-genres/browse-genres-page';
import { BrowseTitlesPageComponent } from './pages/browse-titles/browse-titles-page';
import { CartPageComponent } from './pages/cart/cart-page';
import { CheckoutPageComponent } from './pages/checkout/checkout-page';
import { ConfirmationPageComponent } from './pages/confirmation/confirmation-page';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  // Default landing page: login first.
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: 'main', component: MainPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'signup', component: SignupPageComponent },
  { path: 'movies', component: MoviesPageComponent },
  { path: 'movies/:movieId', component: MovieDetailsPageComponent },
  { path: 'stars/:starId', component: StarDetailsPageComponent },
  { path: 'browse/genres', component: BrowseGenresPageComponent },
  { path: 'browse/titles', component: BrowseTitlesPageComponent },
   { path: 'cart', component: CartPageComponent, canActivate: [authGuard] },
  { path: 'checkout', component: CheckoutPageComponent, canActivate: [authGuard] },
  { path: 'confirmation', component: ConfirmationPageComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: 'login' },
];
