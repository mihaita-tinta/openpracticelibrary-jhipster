import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMediaAsset } from '../media-asset.model';
import { MediaAssetService } from '../service/media-asset.service';

@Component({
  standalone: true,
  templateUrl: './media-asset-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MediaAssetDeleteDialogComponent {
  mediaAsset?: IMediaAsset;

  protected mediaAssetService = inject(MediaAssetService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mediaAssetService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
