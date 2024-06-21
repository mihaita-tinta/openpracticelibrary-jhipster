import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ILinkItem } from '../link-item.model';

@Component({
  standalone: true,
  selector: 'jhi-link-item-detail',
  templateUrl: './link-item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LinkItemDetailComponent {
  linkItem = input<ILinkItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
