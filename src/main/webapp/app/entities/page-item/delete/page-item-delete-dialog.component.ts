import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPageItem } from '../page-item.model';
import { PageItemService } from '../service/page-item.service';

@Component({
  standalone: true,
  templateUrl: './page-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PageItemDeleteDialogComponent {
  pageItem?: IPageItem;

  protected pageItemService = inject(PageItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pageItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
