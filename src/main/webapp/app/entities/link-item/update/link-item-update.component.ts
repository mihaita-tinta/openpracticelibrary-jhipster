import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPracticeItem } from 'app/entities/practice-item/practice-item.model';
import { PracticeItemService } from 'app/entities/practice-item/service/practice-item.service';
import { ILinkItem } from '../link-item.model';
import { LinkItemService } from '../service/link-item.service';
import { LinkItemFormService, LinkItemFormGroup } from './link-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-link-item-update',
  templateUrl: './link-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LinkItemUpdateComponent implements OnInit {
  isSaving = false;
  linkItem: ILinkItem | null = null;

  practiceItemsSharedCollection: IPracticeItem[] = [];

  protected linkItemService = inject(LinkItemService);
  protected linkItemFormService = inject(LinkItemFormService);
  protected practiceItemService = inject(PracticeItemService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LinkItemFormGroup = this.linkItemFormService.createLinkItemFormGroup();

  comparePracticeItem = (o1: IPracticeItem | null, o2: IPracticeItem | null): boolean =>
    this.practiceItemService.comparePracticeItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ linkItem }) => {
      this.linkItem = linkItem;
      if (linkItem) {
        this.updateForm(linkItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const linkItem = this.linkItemFormService.getLinkItem(this.editForm);
    if (linkItem.id !== null) {
      this.subscribeToSaveResponse(this.linkItemService.update(linkItem));
    } else {
      this.subscribeToSaveResponse(this.linkItemService.create(linkItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILinkItem>>): void {
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

  protected updateForm(linkItem: ILinkItem): void {
    this.linkItem = linkItem;
    this.linkItemFormService.resetForm(this.editForm, linkItem);

    this.practiceItemsSharedCollection = this.practiceItemService.addPracticeItemToCollectionIfMissing<IPracticeItem>(
      this.practiceItemsSharedCollection,
      linkItem.practiceItem,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.practiceItemService
      .query()
      .pipe(map((res: HttpResponse<IPracticeItem[]>) => res.body ?? []))
      .pipe(
        map((practiceItems: IPracticeItem[]) =>
          this.practiceItemService.addPracticeItemToCollectionIfMissing<IPracticeItem>(practiceItems, this.linkItem?.practiceItem),
        ),
      )
      .subscribe((practiceItems: IPracticeItem[]) => (this.practiceItemsSharedCollection = practiceItems));
  }
}
