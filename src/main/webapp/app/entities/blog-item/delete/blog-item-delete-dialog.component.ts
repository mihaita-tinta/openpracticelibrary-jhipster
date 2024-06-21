import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBlogItem } from '../blog-item.model';
import { BlogItemService } from '../service/blog-item.service';

@Component({
  standalone: true,
  templateUrl: './blog-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BlogItemDeleteDialogComponent {
  blogItem?: IBlogItem;

  protected blogItemService = inject(BlogItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.blogItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
