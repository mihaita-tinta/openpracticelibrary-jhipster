import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBlogItem, NewBlogItem } from '../blog-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBlogItem for edit and NewBlogItemFormGroupInput for create.
 */
type BlogItemFormGroupInput = IBlogItem | PartialWithRequiredKeyOf<NewBlogItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBlogItem | NewBlogItem> = Omit<T, 'publishDate' | 'approvedDate'> & {
  publishDate?: string | null;
  approvedDate?: string | null;
};

type BlogItemFormRawValue = FormValueOf<IBlogItem>;

type NewBlogItemFormRawValue = FormValueOf<NewBlogItem>;

type BlogItemFormDefaults = Pick<NewBlogItem, 'id' | 'publishDate' | 'approvedDate'>;

type BlogItemFormGroupContent = {
  id: FormControl<BlogItemFormRawValue['id'] | NewBlogItem['id']>;
  title: FormControl<BlogItemFormRawValue['title']>;
  subtitle: FormControl<BlogItemFormRawValue['subtitle']>;
  publishDate: FormControl<BlogItemFormRawValue['publishDate']>;
  publishedBy: FormControl<BlogItemFormRawValue['publishedBy']>;
  status: FormControl<BlogItemFormRawValue['status']>;
  approvedBy: FormControl<BlogItemFormRawValue['approvedBy']>;
  approvedDate: FormControl<BlogItemFormRawValue['approvedDate']>;
  authors: FormControl<BlogItemFormRawValue['authors']>;
  jumbotronAltText: FormControl<BlogItemFormRawValue['jumbotronAltText']>;
  body: FormControl<BlogItemFormRawValue['body']>;
  jumbotronImage: FormControl<BlogItemFormRawValue['jumbotronImage']>;
};

export type BlogItemFormGroup = FormGroup<BlogItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BlogItemFormService {
  createBlogItemFormGroup(blogItem: BlogItemFormGroupInput = { id: null }): BlogItemFormGroup {
    const blogItemRawValue = this.convertBlogItemToBlogItemRawValue({
      ...this.getFormDefaults(),
      ...blogItem,
    });
    return new FormGroup<BlogItemFormGroupContent>({
      id: new FormControl(
        { value: blogItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(blogItemRawValue.title),
      subtitle: new FormControl(blogItemRawValue.subtitle),
      publishDate: new FormControl(blogItemRawValue.publishDate),
      publishedBy: new FormControl(blogItemRawValue.publishedBy),
      status: new FormControl(blogItemRawValue.status),
      approvedBy: new FormControl(blogItemRawValue.approvedBy),
      approvedDate: new FormControl(blogItemRawValue.approvedDate),
      authors: new FormControl(blogItemRawValue.authors),
      jumbotronAltText: new FormControl(blogItemRawValue.jumbotronAltText),
      body: new FormControl(blogItemRawValue.body),
      jumbotronImage: new FormControl(blogItemRawValue.jumbotronImage),
    });
  }

  getBlogItem(form: BlogItemFormGroup): IBlogItem | NewBlogItem {
    return this.convertBlogItemRawValueToBlogItem(form.getRawValue() as BlogItemFormRawValue | NewBlogItemFormRawValue);
  }

  resetForm(form: BlogItemFormGroup, blogItem: BlogItemFormGroupInput): void {
    const blogItemRawValue = this.convertBlogItemToBlogItemRawValue({ ...this.getFormDefaults(), ...blogItem });
    form.reset(
      {
        ...blogItemRawValue,
        id: { value: blogItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BlogItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      publishDate: currentTime,
      approvedDate: currentTime,
    };
  }

  private convertBlogItemRawValueToBlogItem(rawBlogItem: BlogItemFormRawValue | NewBlogItemFormRawValue): IBlogItem | NewBlogItem {
    return {
      ...rawBlogItem,
      publishDate: dayjs(rawBlogItem.publishDate, DATE_TIME_FORMAT),
      approvedDate: dayjs(rawBlogItem.approvedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBlogItemToBlogItemRawValue(
    blogItem: IBlogItem | (Partial<NewBlogItem> & BlogItemFormDefaults),
  ): BlogItemFormRawValue | PartialWithRequiredKeyOf<NewBlogItemFormRawValue> {
    return {
      ...blogItem,
      publishDate: blogItem.publishDate ? blogItem.publishDate.format(DATE_TIME_FORMAT) : undefined,
      approvedDate: blogItem.approvedDate ? blogItem.approvedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
