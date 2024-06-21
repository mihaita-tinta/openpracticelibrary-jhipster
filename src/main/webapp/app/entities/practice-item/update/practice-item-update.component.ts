import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMediaAsset } from 'app/entities/media-asset/media-asset.model';
import { MediaAssetService } from 'app/entities/media-asset/service/media-asset.service';
import { Status } from 'app/entities/enumerations/status.model';
import { FacilitationDifficulty } from 'app/entities/enumerations/facilitation-difficulty.model';
import { MobiusLoopTag } from 'app/entities/enumerations/mobius-loop-tag.model';
import { PracticeItemService } from '../service/practice-item.service';
import { IPracticeItem } from '../practice-item.model';
import { PracticeItemFormService, PracticeItemFormGroup } from './practice-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-practice-item-update',
  templateUrl: './practice-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PracticeItemUpdateComponent implements OnInit {
  isSaving = false;
  practiceItem: IPracticeItem | null = null;
  statusValues = Object.keys(Status);
  facilitationDifficultyValues = Object.keys(FacilitationDifficulty);
  mobiusLoopTagValues = Object.keys(MobiusLoopTag);

  coverImagesCollection: IMediaAsset[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected practiceItemService = inject(PracticeItemService);
  protected practiceItemFormService = inject(PracticeItemFormService);
  protected mediaAssetService = inject(MediaAssetService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PracticeItemFormGroup = this.practiceItemFormService.createPracticeItemFormGroup();

  compareMediaAsset = (o1: IMediaAsset | null, o2: IMediaAsset | null): boolean => this.mediaAssetService.compareMediaAsset(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ practiceItem }) => {
      this.practiceItem = practiceItem;
      if (practiceItem) {
        this.updateForm(practiceItem);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const practiceItem = this.practiceItemFormService.getPracticeItem(this.editForm);
    if (practiceItem.id !== null) {
      this.subscribeToSaveResponse(this.practiceItemService.update(practiceItem));
    } else {
      this.subscribeToSaveResponse(this.practiceItemService.create(practiceItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPracticeItem>>): void {
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

  protected updateForm(practiceItem: IPracticeItem): void {
    this.practiceItem = practiceItem;
    this.practiceItemFormService.resetForm(this.editForm, practiceItem);

    this.coverImagesCollection = this.mediaAssetService.addMediaAssetToCollectionIfMissing<IMediaAsset>(
      this.coverImagesCollection,
      practiceItem.coverImage,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mediaAssetService
      .query({ filter: 'practiceitem-is-null' })
      .pipe(map((res: HttpResponse<IMediaAsset[]>) => res.body ?? []))
      .pipe(
        map((mediaAssets: IMediaAsset[]) =>
          this.mediaAssetService.addMediaAssetToCollectionIfMissing<IMediaAsset>(mediaAssets, this.practiceItem?.coverImage),
        ),
      )
      .subscribe((mediaAssets: IMediaAsset[]) => (this.coverImagesCollection = mediaAssets));
  }
}
