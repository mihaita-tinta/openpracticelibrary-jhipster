import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILinkItem } from '../link-item.model';
import { LinkItemService } from '../service/link-item.service';

@Component({
  standalone: true,
  templateUrl: './link-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LinkItemDeleteDialogComponent {
  linkItem?: ILinkItem;

  protected linkItemService = inject(LinkItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.linkItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
