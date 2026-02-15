import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-confirmation-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './confirmation-page.html'
})
export class ConfirmationPageComponent {
  success = history.state?.success ?? false;
  message = history.state?.message ?? '';
}
