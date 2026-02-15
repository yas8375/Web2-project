import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-browse-titles-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './browse-titles-page.html'
})
export class BrowseTitlesPageComponent {
  letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'.split('');
}
