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
import { BlogItemService } from '../service/blog-item.service';
import { IBlogItem } from '../blog-item.model';
import { BlogItemFormService, BlogItemFormGroup } from './blog-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-blog-item-update',
  templateUrl: './blog-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BlogItemUpdateComponent implements OnInit {
  isSaving = false;
  blogItem: IBlogItem | null = null;
  statusValues = Object.keys(Status);

  jumbotronImagesCollection: IMediaAsset[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected blogItemService = inject(BlogItemService);
  protected blogItemFormService = inject(BlogItemFormService);
  protected mediaAssetService = inject(MediaAssetService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BlogItemFormGroup = this.blogItemFormService.createBlogItemFormGroup();

  compareMediaAsset = (o1: IMediaAsset | null, o2: IMediaAsset | null): boolean => this.mediaAssetService.compareMediaAsset(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blogItem }) => {
      this.blogItem = blogItem;
      if (blogItem) {
        this.updateForm(blogItem);
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
    const blogItem = this.blogItemFormService.getBlogItem(this.editForm);
    if (blogItem.id !== null) {
      this.subscribeToSaveResponse(this.blogItemService.update(blogItem));
    } else {
      this.subscribeToSaveResponse(this.blogItemService.create(blogItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlogItem>>): void {
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

  protected updateForm(blogItem: IBlogItem): void {
    this.blogItem = blogItem;
    this.blogItemFormService.resetForm(this.editForm, blogItem);

    this.jumbotronImagesCollection = this.mediaAssetService.addMediaAssetToCollectionIfMissing<IMediaAsset>(
      this.jumbotronImagesCollection,
      blogItem.jumbotronImage,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mediaAssetService
      .query({ filter: 'blogitem-is-null' })
      .pipe(map((res: HttpResponse<IMediaAsset[]>) => res.body ?? []))
      .pipe(
        map((mediaAssets: IMediaAsset[]) =>
          this.mediaAssetService.addMediaAssetToCollectionIfMissing<IMediaAsset>(mediaAssets, this.blogItem?.jumbotronImage),
        ),
      )
      .subscribe((mediaAssets: IMediaAsset[]) => (this.jumbotronImagesCollection = mediaAssets));
  }
}
