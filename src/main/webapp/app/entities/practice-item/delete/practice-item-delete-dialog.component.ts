import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPracticeItem } from '../practice-item.model';
import { PracticeItemService } from '../service/practice-item.service';

@Component({
  standalone: true,
  templateUrl: './practice-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PracticeItemDeleteDialogComponent {
  practiceItem?: IPracticeItem;

  protected practiceItemService = inject(PracticeItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.practiceItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
