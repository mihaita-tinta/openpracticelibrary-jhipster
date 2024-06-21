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
import { PageItemService } from '../service/page-item.service';
import { IPageItem } from '../page-item.model';
import { PageItemFormService, PageItemFormGroup } from './page-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-page-item-update',
  templateUrl: './page-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PageItemUpdateComponent implements OnInit {
  isSaving = false;
  pageItem: IPageItem | null = null;
  statusValues = Object.keys(Status);

  jumbotronImagesCollection: IMediaAsset[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected pageItemService = inject(PageItemService);
  protected pageItemFormService = inject(PageItemFormService);
  protected mediaAssetService = inject(MediaAssetService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PageItemFormGroup = this.pageItemFormService.createPageItemFormGroup();

  compareMediaAsset = (o1: IMediaAsset | null, o2: IMediaAsset | null): boolean => this.mediaAssetService.compareMediaAsset(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pageItem }) => {
      this.pageItem = pageItem;
      if (pageItem) {
        this.updateForm(pageItem);
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
    const pageItem = this.pageItemFormService.getPageItem(this.editForm);
    if (pageItem.id !== null) {
      this.subscribeToSaveResponse(this.pageItemService.update(pageItem));
    } else {
      this.subscribeToSaveResponse(this.pageItemService.create(pageItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPageItem>>): void {
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

  protected updateForm(pageItem: IPageItem): void {
    this.pageItem = pageItem;
    this.pageItemFormService.resetForm(this.editForm, pageItem);

    this.jumbotronImagesCollection = this.mediaAssetService.addMediaAssetToCollectionIfMissing<IMediaAsset>(
      this.jumbotronImagesCollection,
      pageItem.jumbotronImage,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mediaAssetService
      .query({ filter: 'pageitem-is-null' })
      .pipe(map((res: HttpResponse<IMediaAsset[]>) => res.body ?? []))
      .pipe(
        map((mediaAssets: IMediaAsset[]) =>
          this.mediaAssetService.addMediaAssetToCollectionIfMissing<IMediaAsset>(mediaAssets, this.pageItem?.jumbotronImage),
        ),
      )
      .subscribe((mediaAssets: IMediaAsset[]) => (this.jumbotronImagesCollection = mediaAssets));
  }
}
