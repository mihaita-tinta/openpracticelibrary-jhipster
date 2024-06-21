import { Component, inject, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPracticeItem } from 'app/entities/practice-item/practice-item.model';
import { PracticeItemService } from 'app/entities/practice-item/service/practice-item.service';
import { MediaAssetType } from 'app/entities/enumerations/media-asset-type.model';
import { MediaAssetService } from '../service/media-asset.service';
import { IMediaAsset } from '../media-asset.model';
import { MediaAssetFormService, MediaAssetFormGroup } from './media-asset-form.service';

@Component({
  standalone: true,
  selector: 'jhi-media-asset-update',
  templateUrl: './media-asset-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MediaAssetUpdateComponent implements OnInit {
  isSaving = false;
  mediaAsset: IMediaAsset | null = null;
  mediaAssetTypeValues = Object.keys(MediaAssetType);

  practiceItemsSharedCollection: IPracticeItem[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected mediaAssetService = inject(MediaAssetService);
  protected mediaAssetFormService = inject(MediaAssetFormService);
  protected practiceItemService = inject(PracticeItemService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MediaAssetFormGroup = this.mediaAssetFormService.createMediaAssetFormGroup();

  comparePracticeItem = (o1: IPracticeItem | null, o2: IPracticeItem | null): boolean =>
    this.practiceItemService.comparePracticeItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mediaAsset }) => {
      this.mediaAsset = mediaAsset;
      if (mediaAsset) {
        this.updateForm(mediaAsset);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('openPracticeLibraryApp.error', { ...err, key: 'error.file.' + err.key }),
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mediaAsset = this.mediaAssetFormService.getMediaAsset(this.editForm);
    if (mediaAsset.id !== null) {
      this.subscribeToSaveResponse(this.mediaAssetService.update(mediaAsset));
    } else {
      this.subscribeToSaveResponse(this.mediaAssetService.create(mediaAsset));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMediaAsset>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mediaAsset: IMediaAsset): void {
    this.mediaAsset = mediaAsset;
    this.mediaAssetFormService.resetForm(this.editForm, mediaAsset);

    this.practiceItemsSharedCollection = this.practiceItemService.addPracticeItemToCollectionIfMissing<IPracticeItem>(
      this.practiceItemsSharedCollection,
      mediaAsset.practices,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.practiceItemService
      .query()
      .pipe(map((res: HttpResponse<IPracticeItem[]>) => res.body ?? []))
      .pipe(
        map((practiceItems: IPracticeItem[]) =>
          this.practiceItemService.addPracticeItemToCollectionIfMissing<IPracticeItem>(practiceItems, this.mediaAsset?.practices),
        ),
      )
      .subscribe((practiceItems: IPracticeItem[]) => (this.practiceItemsSharedCollection = practiceItems));
  }
}
