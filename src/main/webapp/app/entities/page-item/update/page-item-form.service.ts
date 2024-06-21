import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPageItem, NewPageItem } from '../page-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPageItem for edit and NewPageItemFormGroupInput for create.
 */
type PageItemFormGroupInput = IPageItem | PartialWithRequiredKeyOf<NewPageItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPageItem | NewPageItem> = Omit<T, 'publishDate' | 'approvedDate'> & {
  publishDate?: string | null;
  approvedDate?: string | null;
};

type PageItemFormRawValue = FormValueOf<IPageItem>;

type NewPageItemFormRawValue = FormValueOf<NewPageItem>;

type PageItemFormDefaults = Pick<NewPageItem, 'id' | 'publishDate' | 'approvedDate'>;

type PageItemFormGroupContent = {
  id: FormControl<PageItemFormRawValue['id'] | NewPageItem['id']>;
  title: FormControl<PageItemFormRawValue['title']>;
  authors: FormControl<PageItemFormRawValue['authors']>;
  menu: FormControl<PageItemFormRawValue['menu']>;
  menuWeight: FormControl<PageItemFormRawValue['menuWeight']>;
  publishDate: FormControl<PageItemFormRawValue['publishDate']>;
  publishedBy: FormControl<PageItemFormRawValue['publishedBy']>;
  status: FormControl<PageItemFormRawValue['status']>;
  approvedBy: FormControl<PageItemFormRawValue['approvedBy']>;
  approvedDate: FormControl<PageItemFormRawValue['approvedDate']>;
  jumbotronAltText: FormControl<PageItemFormRawValue['jumbotronAltText']>;
  body: FormControl<PageItemFormRawValue['body']>;
  jumbotronImage: FormControl<PageItemFormRawValue['jumbotronImage']>;
};

export type PageItemFormGroup = FormGroup<PageItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PageItemFormService {
  createPageItemFormGroup(pageItem: PageItemFormGroupInput = { id: null }): PageItemFormGroup {
    const pageItemRawValue = this.convertPageItemToPageItemRawValue({
      ...this.getFormDefaults(),
      ...pageItem,
    });
    return new FormGroup<PageItemFormGroupContent>({
      id: new FormControl(
        { value: pageItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(pageItemRawValue.title),
      authors: new FormControl(pageItemRawValue.authors),
      menu: new FormControl(pageItemRawValue.menu),
      menuWeight: new FormControl(pageItemRawValue.menuWeight),
      publishDate: new FormControl(pageItemRawValue.publishDate),
      publishedBy: new FormControl(pageItemRawValue.publishedBy),
      status: new FormControl(pageItemRawValue.status),
      approvedBy: new FormControl(pageItemRawValue.approvedBy),
      approvedDate: new FormControl(pageItemRawValue.approvedDate),
      jumbotronAltText: new FormControl(pageItemRawValue.jumbotronAltText),
      body: new FormControl(pageItemRawValue.body),
      jumbotronImage: new FormControl(pageItemRawValue.jumbotronImage),
    });
  }

  getPageItem(form: PageItemFormGroup): IPageItem | NewPageItem {
    return this.convertPageItemRawValueToPageItem(form.getRawValue() as PageItemFormRawValue | NewPageItemFormRawValue);
  }

  resetForm(form: PageItemFormGroup, pageItem: PageItemFormGroupInput): void {
    const pageItemRawValue = this.convertPageItemToPageItemRawValue({ ...this.getFormDefaults(), ...pageItem });
    form.reset(
      {
        ...pageItemRawValue,
        id: { value: pageItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PageItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      publishDate: currentTime,
      approvedDate: currentTime,
    };
  }

  private convertPageItemRawValueToPageItem(rawPageItem: PageItemFormRawValue | NewPageItemFormRawValue): IPageItem | NewPageItem {
    return {
      ...rawPageItem,
      publishDate: dayjs(rawPageItem.publishDate, DATE_TIME_FORMAT),
      approvedDate: dayjs(rawPageItem.approvedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPageItemToPageItemRawValue(
    pageItem: IPageItem | (Partial<NewPageItem> & PageItemFormDefaults),
  ): PageItemFormRawValue | PartialWithRequiredKeyOf<NewPageItemFormRawValue> {
    return {
      ...pageItem,
      publishDate: pageItem.publishDate ? pageItem.publishDate.format(DATE_TIME_FORMAT) : undefined,
      approvedDate: pageItem.approvedDate ? pageItem.approvedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
